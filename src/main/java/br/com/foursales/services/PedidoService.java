package br.com.foursales.services;

import br.com.foursales.autentication.services.exceptions.FoursalesBusinessException;
import br.com.foursales.dao.PedidoDAO;
import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.dto.PagamentoPedidoResponseDTO;
import br.com.foursales.dto.StatusPedidoEnum;
import br.com.foursales.model.ItemPedidoEntity;
import br.com.foursales.model.PedidoEntity;
import br.com.foursales.model.ProdutoEntity;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class PedidoService {
    private final PedidoDAO pedidoDAO;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PedidoService(PedidoDAO pedidoDAO, KafkaTemplate<String, String> kafkaTemplate) {
        this.pedidoDAO = pedidoDAO;
        this.kafkaTemplate = kafkaTemplate;
    }



    @Transactional
    public PedidoEntity criarPedido(PedidoEntity pedidoEntity) {

        PedidoEntity savedPedidoEntity = pedidoDAO.save(pedidoEntity);
        kafkaTemplate.send("order.created", "Pedido criado: " + savedPedidoEntity.getId());
        return savedPedidoEntity;
    }

    public PedidoEntity pagarPedido(Long pedidoId, BigDecimal valorTotalPago) {

        PedidoEntity pedidoEntity = pedidoDAO.findById(pedidoId).orElseThrow();
        List<ItemPedidoEntity> itensPedido = pedidoEntity.getItens().stream().toList();

        if(pedidoEntity.getIdStatus().equals(StatusPedidoEnum.PENDENTE)){

            BigDecimal valorTotalPedido = new BigDecimal(0);
            itensPedido.forEach(i->{
                valorTotalPedido.add(new BigDecimal(i.getProdutoEntity().getPreco()));
            });

            if(valorTotalPago.compareTo(valorTotalPedido) >= 0 ){
                pedidoEntity.setValorPagoPedido(valorTotalPago);
                pedidoEntity.setValorTotalPedido(valorTotalPedido);
                itensPedido.forEach(i->{
                    i.setValorPagoItem(new BigDecimal(i.getProdutoEntity().getPreco()));
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