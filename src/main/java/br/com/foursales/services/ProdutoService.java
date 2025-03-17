package br.com.foursales.services;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
import br.com.foursales.dao.ProdutoDAO;
import br.com.foursales.elasticsearch.dao.ProdutoElasticsearchDAO;
import br.com.foursales.model.ProdutoDocument;
import br.com.foursales.model.ProdutoEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    private final ProdutoDAO produtoDAO;

    private final ProdutoElasticsearchDAO produtoElasticsearchDAO;

    public ProdutoService(ProdutoDAO produtoDAO, ProdutoElasticsearchDAO produtoElasticsearchDAO) {
        this.produtoDAO = produtoDAO;
        this.produtoElasticsearchDAO = produtoElasticsearchDAO;
    }

    public List<ProdutoEntity> listarProdutos() {
        List<ProdutoEntity> produtoEntities = (List<ProdutoEntity>) produtoDAO.findAll();
        return produtoEntities;
    }

    public ProdutoEntity criarProduto(ProdutoEntity produto){
        ProdutoEntity prod = null;
        try {
            prod =  produtoDAO.save(produto);
            produtoElasticsearchDAO.save(new ProdutoDocument(prod.getId(), prod.getNome(), prod.getCategoria(), prod.getPreco().doubleValue(), prod.getQtdEstoque()));

        } catch (DataIntegrityViolationException er){
            throw new FourSalesBusinessException("Produto já existente, tente outro!");
        } catch (Exception e){
            logger.error("Erro inesperado ao cadastrar o produto: {}", e.getMessage());
            throw new RuntimeException("Erro inesperado ao cadastrar o produto: ", e);
        }

          return  prod;
    }


    public ProdutoEntity atualizaProduto(ProdutoEntity produto){

        ProdutoEntity produtoExistente = produtoDAO.findById(produto.getId())
                .orElseThrow(() -> new FourSalesBusinessException("Produto não existe"));

        ProdutoDocument produtoExistenteElastic = produtoElasticsearchDAO.findById(produtoExistente.getId()).orElse(
                new ProdutoDocument(produtoExistente.getId(),produtoExistente.getNome(),produtoExistente.getCategoria(),
                        produtoExistente.getPreco().doubleValue(),produtoExistente.getQtdEstoque())
        );

        // Atualiza apenas os campos não nulos
        if (produto.getNome() != null) {
            produtoExistenteElastic.setNome(produto.getNome());
            produtoExistente.setNome(produto.getNome());
        }
        if (produto.getPreco() != null) {
            produtoExistente.setPreco(produto.getPreco());
            produtoExistenteElastic.setPreco(produto.getPreco().doubleValue());
        }
        if (produto.getQtdEstoque() != null) {
            produtoExistente.setQtdEstoque(produto.getQtdEstoque());
            produtoExistenteElastic.setQtdEstoque(produto.getQtdEstoque());
        }
        if (produto.getCategoria() != null) {
            produtoExistente.setCategoria(produto.getCategoria());
            produtoExistenteElastic.setCategoria(produto.getCategoria());
        }

        produtoExistente = produtoDAO.save(produtoExistente);
        produtoElasticsearchDAO.save(produtoExistenteElastic);
        return  produtoExistente;
    }
    public void excluiProduto(Integer id) throws Exception {
        if(produtoDAO.existsById(id))
             produtoDAO.deleteById(id);
        else
            throw new Exception("Produto não existe");
    }

}