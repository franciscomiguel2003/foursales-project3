package br.com.foursales.services;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
import br.com.foursales.dao.PedidoDAO;
import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.dto.*;
import br.com.foursales.elasticsearch.dao.ProdutoElasticsearchDAO;
import br.com.foursales.email.services.GmailService;
import br.com.foursales.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PedidoService {
    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    private final PedidoDAO pedidoDAO;
    private final ProdutoDAO produtoDAO;

    private final ProdutoElasticsearchDAO produtoElasticsearchDAO;
    private final KafkaTemplate<String, Long> kafkaTemplate;

    private final GmailService gmailService;

    public PedidoService(PedidoDAO pedidoDAO, ProdutoDAO produtoDAO, ProdutoElasticsearchDAO produtoElasticsearchDAO, KafkaTemplate<String, Long> kafkaTemplate, GmailService gmailService) {
        this.pedidoDAO = pedidoDAO;
        this.produtoDAO = produtoDAO;
        this.produtoElasticsearchDAO = produtoElasticsearchDAO;
        this.kafkaTemplate = kafkaTemplate;
        this.gmailService = gmailService;
    }



    @Transactional
    public PedidoResponseDTO criarPedido(UserEntity user, List<ItemPedidoDTO> itemPedidoListDTO) {

        PedidoResponseDTO pedidoResponseDTO = null;
        PedidoEntity pedido = new PedidoEntity();
        pedido.setUser(user);
        pedido.setIdStatus(StatusPedidoEnum.PENDENTE.getIdStatus());

        List<ItemPedidoResponseDTO> listItemPedidoResponseDTO = new ArrayList<>();
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        HashMap<Integer, Integer> produtosHash = new HashMap<Integer, Integer>();

        if (itemPedidoListDTO != null && !itemPedidoListDTO.isEmpty()) {

            //Juntando Produtos adicionados mais de 1x na lista
            for (ItemPedidoDTO dto : itemPedidoListDTO) {
                if(!produtosHash.containsKey(dto.idProduto()))
                    produtosHash.put(dto.idProduto(),dto.qtd());
                else
                    produtosHash.put(dto.idProduto(),produtosHash.get(dto.idProduto())+dto.qtd());

            }

            for (Integer idProduto : produtosHash.keySet()) {
                int qtdProduto =produtosHash.get(idProduto);

                ProdutoEntity produto = produtoDAO.findById(idProduto)
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + idProduto));

                if(produto.getQtdEstoque()<qtdProduto)
                    throw new FourSalesBusinessException("Não foi possível criar o pedido pois a quantidade de "
                            + produto.getNome() + " no estoque é: " + produto.getQtdEstoque());

                BigDecimal valorTotalItem = produto.getPreco();

                ItemPedidoEntity item = new ItemPedidoEntity();
                item.setProdutoEntity(produto);
                item.setQtd(qtdProduto);
                item.setPedidoEntity(pedido);
                pedido.getItens().add(item);
                item.setValorTotalItem(valorTotalItem);

                valorTotalItem = valorTotalItem.multiply(new BigDecimal(produtosHash.get(idProduto)));

                valorTotalPedido = valorTotalPedido.add(valorTotalItem);
                pedido.setValorTotal(valorTotalPedido);

                ItemPedidoResponseDTO itemDTO = new ItemPedidoResponseDTO(produto.getNome(), produto.getPreco(), valorTotalItem,  qtdProduto);
                listItemPedidoResponseDTO.add(itemDTO);
            }

            pedido = pedidoDAO.save(pedido);
            kafkaTemplate.send("order.created", pedido.getId());
            pedidoResponseDTO = new PedidoResponseDTO(pedido.getId(), valorTotalPedido, StatusPedidoEnum.PENDENTE.name(),listItemPedidoResponseDTO);

        } else {
            throw new FourSalesBusinessException("Não existem itens no seu pedido");
        }


        return pedidoResponseDTO;
    }

    @Transactional(noRollbackForClassName = "FourSalesWarningException.class")
    public PedidoEntity buscarPedidosPorId(Long idPedido){

        PedidoEntity pedido = pedidoDAO.findById(idPedido).orElse(null);
        return pedido;

    }
    @Transactional
    public PagamentoPedidoResponseDTO pagarPedido(Long pedidoId, BigDecimal valorTotalPago) {

        String msgRetorno="";
        PedidoResponseDTO pedidoResponseDTO;

        PedidoEntity pedidoEntity = pedidoDAO.findById(pedidoId).orElseThrow();
        List<ItemPedidoEntity> itensPedido = pedidoEntity.getItens();
        List<ItemPedidoResponseDTO> listItemPedidoResponseDTO = new ArrayList<>();

        if(pedidoEntity.getIdStatus().equals(StatusPedidoEnum.PENDENTE.getIdStatus())){

            BigDecimal valorTotalPedido = BigDecimal.ZERO;

            for (ItemPedidoEntity i: itensPedido) {
                BigDecimal valorItem = i.getProdutoEntity().getPreco().multiply(new BigDecimal(i.getQtd()));
                valorTotalPedido = valorTotalPedido.add(valorItem);
                i.setValorTotalItem(valorItem);

                ProdutoEntity prod = i.getProdutoEntity();

                ItemPedidoResponseDTO itemDTO = new ItemPedidoResponseDTO(prod.getNome(),
                        prod.getPreco(), valorItem,  i.getQtd());

                listItemPedidoResponseDTO.add(itemDTO);
            }

            if(valorTotalPago.compareTo(valorTotalPedido) >= 0 ){
                pedidoEntity.setValorTotal(valorTotalPedido);
                pedidoEntity.setValorTotalPago(valorTotalPago);
                pedidoEntity.setIdStatus(StatusPedidoEnum.PAGO.getIdStatus());

                msgRetorno = "Pedido pago com sucesso! ";

                if(valorTotalPago.compareTo(valorTotalPedido) > 0)
                    msgRetorno = msgRetorno + "O valor pago é mais alto que o valor do pedido!";


                pedidoDAO.save(pedidoEntity);

                kafkaTemplate.send("order.paid", pedidoEntity.getId());

            }  else {
                pedidoEntity.setIdStatus(StatusPedidoEnum.CANDELADO.getIdStatus());
                pedidoDAO.save(pedidoEntity);
                msgRetorno = "Seu pedido foi cancelado pois o valor do pagamento" +
                        " é menor que o valor do pedido";

            }

        } else {
            throw new FourSalesBusinessException("O status atual não permite alteração");
        }

        pedidoResponseDTO = new PedidoResponseDTO(pedidoEntity.getId(),pedidoEntity.getValorTotal(),StatusPedidoEnum.PAGO.name(),listItemPedidoResponseDTO);

        return new PagamentoPedidoResponseDTO(pedidoResponseDTO,msgRetorno);
    }

    //Atualiza estoque garantindo a quantidade inclusive com transações simultâneas
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizaEstoqueProduto(ItemPedidoEntity itemPedido, PedidoEntity pedido){
            //Verifica produtos no estoque para no caso de transações simultaneas
            ProdutoEntity produto = produtoDAO.findByIdWithLock(itemPedido.getProdutoEntity().getId());
            ProdutoDocument produtoElasticsearch = produtoElasticsearchDAO.findById(itemPedido.getProdutoEntity().getId()).orElse(
                    new ProdutoDocument(produto.getId(),produto.getNome(),produto.getCategoria(),produto.getPreco().doubleValue(),produto.getQtdEstoque())
            );

            UserEntity user = pedido.getUser();

            if(produto.getQtdEstoque().intValue()>=itemPedido.getQtd()) {
                produto.setQtdEstoque(produto.getQtdEstoque().intValue() - itemPedido.getQtd());
                produtoElasticsearch.setQtdEstoque(produto.getQtdEstoque());
                produtoDAO.save(produto);
                produtoElasticsearchDAO.save(produtoElasticsearch);
            } else {
                cancelaPedido(pedido);
                gmailService.enviarEmail(user.getEmail(), "O pedido de número: " +pedido.getId() +
                                " foi CANCELADO",
                        "Seu pedido foi cancelado por falta do produto em estoque");

            }

        logger.info("Estoque do produto atualizado: idProduto{}", produto.getId());
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void cancelaPedido(PedidoEntity pedido){

        pedido.setIdStatus(StatusPedidoEnum.CANDELADO.getIdStatus());
        pedidoDAO.save(pedido);

    }

    public List<TotalFaturadoMesResponseDTO> buscarTotalFaturadoMes(String mesAnoReferencia){

        //Ajusta data se necessario
        String mesAno = mesAnoReferencia.length() <6?"0"+mesAnoReferencia:mesAnoReferencia;
        int mes = Integer.parseInt(mesAno.substring(0,2));
        int ano = Integer.parseInt(mesAno.substring(2,6));

        LocalDate dataRef = LocalDate.now();
        dataRef = dataRef.withDayOfMonth(1);
        dataRef = dataRef.withMonth(mes);
        dataRef = dataRef.withYear(ano);

        LocalDate dataInicio = dataRef.withDayOfMonth(1);
        LocalDate dataFim = dataInicio.plusMonths(1);
        return pedidoDAO.buscarTotalFaturadoMes(dataInicio, dataFim);
    }
    public List<TicketMedioUsuarioResponseDTO> buscarTicketMedioUsuario(){
        return pedidoDAO.buscarTicketMedioUsuario();

    }

    public List<Top5UsuariosComprasResponseDTO> buscarTop5UsuariosCompras(){
        return pedidoDAO.buscaTop5MelhoresUsuarios();
    }

}