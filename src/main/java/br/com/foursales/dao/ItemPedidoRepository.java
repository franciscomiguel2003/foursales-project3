package br.com.foursales.dao;

import br.com.foursales.model.ItemPedido;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemPedidoRepository extends CrudRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedidoId(Long pedidoId);
}