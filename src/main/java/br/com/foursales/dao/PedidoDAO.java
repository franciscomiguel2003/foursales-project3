package br.com.foursales.dao;

import br.com.foursales.model.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PedidoDAO extends JpaRepository<PedidoEntity, Long> {}
