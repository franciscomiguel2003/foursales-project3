package br.com.foursales.dao;

import br.com.foursales.model.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemPedidoDAO extends JpaRepository<ItemPedidoEntity, Long> {
    List<ItemPedidoEntity> findByPedidoEntityId(Long pedidoId);
}