package br.com.foursales.controller;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
import br.com.foursales.dto.ErrorResponseDTO;
import br.com.foursales.dto.ResponseFourSales;
import br.com.foursales.model.ProdutoDocument;
import br.com.foursales.model.ProdutoEntity;
import br.com.foursales.services.ProdutoServiceElasticSearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produto/elastic")
public class ProdutoElasticSearchController {

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    private final ProdutoServiceElasticSearch produtoServiceElasticSearch;

    public ProdutoElasticSearchController(ProdutoServiceElasticSearch produtoServiceElasticSearch) {
        this.produtoServiceElasticSearch = produtoServiceElasticSearch;
    }

    @GetMapping("/buscaProdutosEmEstoqueElasticSearch")
    public ResponseEntity buscaProdutosEmEstoqueElasticSearch(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "precoMin", required = false) Double precoMin,
            @RequestParam(value = "precoMax", required = false) Double precoMax) {


        try {

            List<ProdutoDocument> listDocument = produtoServiceElasticSearch.buscaProdutosEmEstoqueElasticSearch(nome, categoria, precoMin, precoMax);
            return ResponseFourSales.getResponse(listDocument, "Busca retornada com sucesso!", HttpStatus.OK);

        } catch (Exception e ){
            String error = "Erro ao buscar produtos no Elastic";
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/findAll")
    public ResponseEntity findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax) {

        try {

            return ResponseFourSales.getResponse(produtoServiceElasticSearch.findAll(nome, categoria, precoMin, precoMax), "Busca retornada com sucesso!", HttpStatus.OK);

        } catch (Exception e ){
            String error = "Erro ao buscar produtos no Elastic";
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
