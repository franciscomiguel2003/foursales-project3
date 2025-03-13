package br.com.foursales.services;

import br.com.foursales.dao.PedidoDAO;
import br.com.foursales.model.PedidoEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private final PedidoDAO pedidoDAO;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PedidoService(PedidoDAO pedidoDAO, KafkaTemplate<String, String> kafkaTemplate) {
        this.pedidoDAO = pedidoDAO;
        this.kafkaTemplate = kafkaTemplate;
    }

    public PedidoEntity criarPedido(PedidoEntity pedidoEntity) {
        PedidoEntity savedPedidoEntity = pedidoDAO.save(pedidoEntity);
        kafkaTemplate.send("order.created", "Pedido criado: " + savedPedidoEntity.getId());
        return savedPedidoEntity;
    }

    public void pagarPedido(Long pedidoId) {
        PedidoEntity pedidoEntity = pedidoDAO.findById(pedidoId).orElseThrow();
        pedidoEntity.setPago(true);
        pedidoDAO.save(pedidoEntity);
        kafkaTemplate.send("order.paid", "Pedido pago: " + pedidoEntity.getId());
    }
}