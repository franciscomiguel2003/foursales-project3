package br.com.foursales.elasticsearch.dao;


import br.com.foursales.model.ProdutoDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProdutoElasticsearchDAO extends ElasticsearchRepository<ProdutoDocument, Integer> {

}
