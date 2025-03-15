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

    public ProdutoEntity criarProduto(ProdutoEntity produto){
        return  produtoDAO.save(produto);
    }

    public ProdutoEntity atualizaProduto(ProdutoEntity produto){
        return  produtoDAO.save(produto);
    }
    public void excluiProduto(Integer id) throws Exception {
        if(produtoDAO.existsById(id))
             produtoDAO.deleteById(id);
        else
            throw new Exception("Produto não existe");
    }

}