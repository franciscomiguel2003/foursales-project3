package br.com.foursales.elasticsearch.dao;

import br.com.foursales.model.ProdutoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProdutoElasticsearchDAO extends ElasticsearchRepository<ProdutoDocument, Long> {}
