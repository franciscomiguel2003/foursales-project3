package br.com.foursales.dao;

import br.com.foursales.model.ProdutoEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoDAO extends CrudRepository<ProdutoEntity, Long> {}
