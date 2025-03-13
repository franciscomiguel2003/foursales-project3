package br.com.foursales.controller;

import br.com.foursales.model.Produto;
import br.com.foursales.services.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/listarProdutos")
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

}
