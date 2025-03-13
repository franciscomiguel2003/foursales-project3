package br.com.foursales.services;

import br.com.foursales.dao.ItemPedidoRepository;
import br.com.foursales.dao.ProdutoRepository;
import br.com.foursales.model.ItemPedido;
import br.com.foursales.model.Produto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class EstoqueService {
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public EstoqueService(ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @KafkaListener(topics = "order.paid", groupId = "estoque-group")
    public void atualizarEstoque(String message) {
        Long pedidoId = Long.parseLong(message.replace("Pedido pago: ", ""));
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedidoId);
        for (ItemPedido item : itens) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoRepository.save(produto);
        }
    }
}

