package br.com.foursales.services;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
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

        ProdutoEntity produtoExistente = produtoDAO.findById(produto.getId())
                .orElseThrow(() -> new FourSalesBusinessException("Produto não existe"));

        // Atualiza apenas os campos não nulos
        if (produto.getNome() != null) {
            produtoExistente.setNome(produto.getNome());
        }
        if (produto.getPreco() != null) {
            produtoExistente.setPreco(produto.getPreco());
        }
        if (produto.getQtdEstoque() != null) {
            produtoExistente.setQtdEstoque(produto.getQtdEstoque());
        }
        if (produto.getCategoria() != null) {
            produtoExistente.setCategoria(produto.getCategoria());
        }

        return  produtoDAO.save(produtoExistente);
    }
    public void excluiProduto(Integer id) throws Exception {
        if(produtoDAO.existsById(id))
             produtoDAO.deleteById(id);
        else
            throw new Exception("Produto não existe");
    }

}