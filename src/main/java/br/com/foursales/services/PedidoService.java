package br.com.foursales.services;

import br.com.foursales.dao.PedidoRepository;
import br.com.foursales.model.Pedido;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PedidoService(PedidoRepository pedidoRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Pedido criarPedido(Pedido pedido) {
        Pedido savedPedido = pedidoRepository.save(pedido);
        kafkaTemplate.send("order.created", "Pedido criado: " + savedPedido.getId());
        return savedPedido;
    }

    public void pagarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
        pedido.setPago(true);
        pedidoRepository.save(pedido);
        kafkaTemplate.send("order.paid", "Pedido pago: " + pedido.getId());
    }
}