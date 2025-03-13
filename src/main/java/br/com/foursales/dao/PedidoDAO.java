package br.com.foursales.dao;

import br.com.foursales.model.PedidoEntity;
import org.springframework.data.repository.CrudRepository;

public interface PedidoDAO extends CrudRepository<PedidoEntity, Long> {}
