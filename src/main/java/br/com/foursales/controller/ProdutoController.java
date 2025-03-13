package br.com.foursales.controller;

import br.com.foursales.model.ProdutoEntity;
import br.com.foursales.services.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/listarProdutos")
    public List<ProdutoEntity> listarProdutos() {
        return produtoService.listarProdutos();
    }

}
