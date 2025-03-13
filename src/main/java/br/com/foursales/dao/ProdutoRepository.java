package br.com.foursales.dao;

import br.com.foursales.model.Produto;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {}
