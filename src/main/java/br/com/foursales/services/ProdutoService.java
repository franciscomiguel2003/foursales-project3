package br.com.foursales.services;

import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.model.ProdutoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoDAO produtoDAO;



    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;

    }

    public List<ProdutoEntity> listarProdutos() {
        List<ProdutoEntity> produtoEntities = (List<ProdutoEntity>) produtoDAO.findAll();
        return produtoEntities;
    }

}