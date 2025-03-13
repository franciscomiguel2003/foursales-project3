package br.com.foursales.services;

import br.com.foursales.dao.ProdutoRepository;
import br.com.foursales.elasticsearch.dao.ProdutoElasticsearchRepository;
import br.com.foursales.model.Produto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;



    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;

    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = (List<Produto>) produtoRepository.findAll();
        return produtos;
    }

}