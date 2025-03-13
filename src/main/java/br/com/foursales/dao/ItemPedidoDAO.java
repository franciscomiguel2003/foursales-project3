package br.com.foursales.dao;

import br.com.foursales.model.ItemPedidoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemPedidoDAO extends CrudRepository<ItemPedidoEntity, Long> {
    List<ItemPedidoEntity> findByPedidoId(Long pedidoId);
}