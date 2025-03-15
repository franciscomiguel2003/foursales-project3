package br.com.foursales.services;

import br.com.foursales.autentication.services.exceptions.FoursalesBusinessException;
import br.com.foursales.dao.PedidoDAO;
import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.dto.ItemPedidoDTO;
import br.com.foursales.dto.ItemPedidoResponseDTO;
import br.com.foursales.dto.PedidoResponseDTO;
import br.com.foursales.dto.StatusPedidoEnum;
import br.com.foursales.model.ItemPedidoEntity;
import br.com.foursales.model.PedidoEntity;
import br.com.foursales.model.ProdutoEntity;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoDAO pedidoDAO;
    private final ProdutoDAO produtoDAO;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PedidoService(PedidoDAO pedidoDAO, ProdutoDAO produtoDAO, KafkaTemplate<String, String> kafkaTemplate) {
        this.pedidoDAO = pedidoDAO;
        this.produtoDAO = produtoDAO;
        this.kafkaTemplate = kafkaTemplate;
    }



    @Transactional
    public PedidoResponseDTO criarPedido(Integer idUser, StatusPedidoEnum statusPedido, List<ItemPedidoDTO> itemPedidoListDTO) {

        PedidoEntity pedido = new PedidoEntity();
        pedido.setIdUser(idUser);
        pedido.setIdStatus(statusPedido.getIdStatus());

        List<ItemPedidoResponseDTO> listItemPedidoResponseDTO = new ArrayList<>();
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        if (itemPedidoListDTO != null && !itemPedidoListDTO.isEmpty()) {
            for (ItemPedidoDTO dto : itemPedidoListDTO) {
                ProdutoEntity produto = produtoDAO.findById(dto.idProduto())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + dto.idProduto()));
                ItemPedidoEntity item = new ItemPedidoEntity();
                item.setProdutoEntity(produto);
                item.setQtd(dto.qtd());
                item.setPedidoEntity(pedido);
                pedido.getItens().add(item);

                BigDecimal valorTotalItem = produto.getPreco();
                valorTotalItem = valorTotalItem.multiply(new BigDecimal(dto.qtd()));

                valorTotalPedido = valorTotalPedido.add(valorTotalItem);

                ItemPedidoResponseDTO itemDTO = new ItemPedidoResponseDTO(produto.getNome(), produto.getPreco(), valorTotalItem,  dto.qtd());
                listItemPedidoResponseDTO.add(itemDTO);
            }
        }

        pedido = pedidoDAO.save(pedido);

        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(pedido.getId(), valorTotalPedido, statusPedido.name(),listItemPedidoResponseDTO);

        kafkaTemplate.send("order.created", "Pedido criado: " + pedido.getId());


        return pedidoResponseDTO;
    }

    @Transactional
    public PedidoEntity buscarPedidosPorId(Long idPedido){

        PedidoEntity pedido = pedidoDAO.findById(idPedido).orElse(null);
        return pedido;

    }
    public PedidoEntity pagarPedido(Long pedidoId, BigDecimal valorTotalPago) {

        PedidoEntity pedidoEntity = pedidoDAO.findById(pedidoId).orElseThrow();
        List<ItemPedidoEntity> itensPedido = pedidoEntity.getItens().stream().toList();

        if(pedidoEntity.getIdStatus().equals(StatusPedidoEnum.PENDENTE)){

            BigDecimal valorTotalPedido = new BigDecimal(0);
            itensPedido.forEach(i->{
                valorTotalPedido.add(i.getProdutoEntity().getPreco());
            });

            if(valorTotalPago.compareTo(valorTotalPedido) >= 0 ){
                pedidoEntity.setValorPagoPedido(valorTotalPago);
                pedidoEntity.setValorTotalPedido(valorTotalPedido);
                itensPedido.forEach(i->{
                    i.setValorPago(i.getProdutoEntity().getPreco());
                });
                pedidoEntity.setIdStatus(StatusPedidoEnum.PAGO.getIdStatus());
            }  else {
                pedidoEntity.setIdStatus(StatusPedidoEnum.CANDELADO.getIdStatus());
                throw new FoursalesBusinessException("Seu pedido será cancelado pois o pagamento efetuado" +
                        " é menor que o valor do pedido");
            }

        }

        pedidoDAO.save(pedidoEntity);
        kafkaTemplate.send("order.paid", "Pedido pago: " + pedidoEntity.getId());
        return pedidoEntity;
    }
}