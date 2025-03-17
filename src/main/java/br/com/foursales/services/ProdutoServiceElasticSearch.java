package br.com.foursales.services;


import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.elasticsearch.dao.ProdutoElasticsearchDAO;
import br.com.foursales.model.ProdutoDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceElasticSearch {

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    private final ProdutoElasticsearchDAO produtoElasticsearchDAO;
    private final ElasticsearchOperations elasticsearchOperations;

    public ProdutoServiceElasticSearch(ProdutoDAO produtoDAO, ProdutoElasticsearchDAO produtoElasticsearchDAO, ElasticsearchOperations elasticsearchOperations) {
        this.produtoElasticsearchDAO = produtoElasticsearchDAO;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<ProdutoDocument> buscaProdutosEmEstoqueElasticSearch(String nome, String categoria,
                                                                     Double precoMin, Double precoMax) {
        logger.info("Buscando produtos com filtros - nome: {}, categoria: {}, precoMin: {}, precoMax: {}",
                nome, categoria, precoMin, precoMax);

        // Aplicar lógica para tratar null
        precoMin = (precoMin == null) ? 0.0 : precoMin;
        precoMax = (precoMax == null) ? Double.MAX_VALUE : precoMax;
        String nomeSafe = (nome != null && !nome.isEmpty()) ? nome : "";
        String categoriaSafe = (categoria != null && !categoria.isEmpty()) ? categoria : "";

        // Construir os critérios
        Criteria criteria = new Criteria();

        // Filtros "must" para nome e categoria com startsWith (equivalente a match_phrase_prefix)
        if (!nomeSafe.isEmpty()) {
            criteria.and(Criteria.where("nome").startsWith(nomeSafe));
        }
        if (!categoriaSafe.isEmpty()) {
            criteria.and(Criteria.where("categoria").startsWith(categoriaSafe));
        }

        // Filtro de intervalo para preço
        Criteria precoCriteria = Criteria.where("preco")
                .greaterThanEqual(precoMin)
                .lessThanEqual(precoMax);
        criteria.and(precoCriteria);

        // Filtro para qtdEstoque > 0
        criteria.and(Criteria.where("qtdEstoque").greaterThan(0));

        // Criar a query
        CriteriaQuery query = new CriteriaQuery(criteria);

        // Executar a busca
        SearchHits<ProdutoDocument> searchHits = elasticsearchOperations.search(
                query, ProdutoDocument.class, IndexCoordinates.of("produtos")
        );

        // Mapear os resultados
        List<ProdutoDocument> resultados = searchHits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        logger.debug("Encontrados {} produtos", resultados.size());
        return resultados;
    }
    public List<ProdutoDocument> findAll(String nome, String categoria, Double precoMin, Double precoMax) {

        List<ProdutoDocument> listProdutos = new ArrayList<>();
        Iterable<ProdutoDocument> produtos = produtoElasticsearchDAO.findAll();
        produtos.forEach(prod ->  listProdutos.add(prod));

        return listProdutos;
    }

}