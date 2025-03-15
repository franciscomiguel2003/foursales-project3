package br.com.foursales.services;

import br.com.foursales.dao.ItemPedidoDAO;
import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.model.ItemPedidoEntity;
import br.com.foursales.model.ProdutoEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
    private final ProdutoDAO produtoDAO;
    private final ItemPedidoDAO itemPedidoDao;


    public EstoqueService(ProdutoDAO produtoDAO, ItemPedidoDAO itemPedidoDao) {
        this.produtoDAO = produtoDAO;
        this.itemPedidoDao = itemPedidoDao;
    }

    @KafkaListener(topics = "order.paid", groupId = "estoque-group")
    public void atualizarEstoque(String message) {
        Long pedidoId = Long.parseLong(message.replace("Pedido pago: ", ""));
        List<ItemPedidoEntity> itens = itemPedidoDao.findByPedidoEntityId(pedidoId);
        for (ItemPedidoEntity item : itens) {
            ProdutoEntity produtoEntity = item.getProdutoEntity();
            produtoEntity.setQtdEstoque(produtoEntity.getQtdEstoque() - item.getQtd());
            produtoDAO.save(produtoEntity);
        }
    }
}

