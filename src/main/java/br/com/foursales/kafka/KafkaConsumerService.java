package br.com.foursales.kafka;

import br.com.foursales.dao.PedidoDAO;
import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.dto.ProdutoEditRequestDTO;
import br.com.foursales.model.ItemPedidoEntity;
import br.com.foursales.model.PedidoEntity;
import br.com.foursales.model.ProdutoEntity;
import br.com.foursales.services.PedidoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class KafkaConsumerService {

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private ProdutoDAO produtoDAO;

    @Autowired
    PedidoService pedidoService;

    @KafkaListener(topics = "order.paid", groupId = "foursales-group")
    @Transactional
    public void consumirAtualizacaoProduto(Long idPedido) {
        logger.info("Mensagem recebida: {}", idPedido);

        PedidoEntity pedido  = pedidoDAO.findById(idPedido).orElseThrow(() ->
                new IllegalArgumentException("Pedido não encontrado: " + idPedido));

        List<ItemPedidoEntity> listItemPedido  = pedido.getItens();

        Hibernate.initialize(pedido.getUser()); //carregar a informação pois está Lazy
        Hibernate.initialize(pedido.getItens());//carregar a informação pois está Lazy

        for (ItemPedidoEntity item: listItemPedido) {
            Hibernate.initialize(item.getProdutoEntity()); //carregar a informação pois está Lazy
            pedidoService.atualizaEstoqueProduto(item, pedido);
        }

        logger.info("Estoque atualizado: de acordo com os produtos do pedido {}", pedido.getId());
    }

}