package br.com.foursales.controller;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
import br.com.foursales.dto.ErrorResponseDTO;
import br.com.foursales.dto.ProdutoEditRequestDTO;
import br.com.foursales.dto.ProdutoRequestDTO;
import br.com.foursales.dto.ResponseFourSales;
import br.com.foursales.model.ProdutoEntity;
import br.com.foursales.services.ProdutoService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produto")
public class ProdutoController {

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/listarProdutos")
    public List<ProdutoEntity> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @PostMapping("/criarProduto")
    public ResponseEntity criarProduto(@RequestBody @Valid ProdutoRequestDTO prodDTO) {


        try {
            ProdutoEntity prod = new ProdutoEntity(null, prodDTO.nome(),prodDTO.categoria(),prodDTO.preco(),prodDTO.qtdEstoque());
            prod = produtoService.criarProduto(prod);
            return ResponseFourSales.getResponse(prod, "Produto cadastrado com sucesso!",HttpStatus.OK);
        } catch (Exception e ){
            String error = "Produto não cadastrado";
            if(e instanceof FourSalesBusinessException)
                error=e.getMessage();
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/editaProduto")
    public ResponseEntity editaProduto(@RequestBody @Valid ProdutoEditRequestDTO prodDTO) {

        try {
            ProdutoEntity prod = new ProdutoEntity(prodDTO.id(), prodDTO.nome(),prodDTO.categoria(),prodDTO.preco(),prodDTO.qtdEstoque());
            ProdutoEntity produtoEntity = produtoService.atualizaProduto(prod);
            return ResponseFourSales.getResponse(produtoEntity, "Produto atualizado com sucesso!",HttpStatus.OK);
        } catch (Exception e ){
            String error = "ERRO AO EDITAR PRODUTO";

            if(e instanceof FourSalesBusinessException)
                error=e.getMessage();

            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/excluiProduto")
    public ResponseEntity excluiProduto(@RequestParam Integer id) {

        try {
            produtoService.excluiProduto(id);
            return ResponseFourSales.getResponse(null, "Produto excluído com sucesso!",HttpStatus.OK);
        }catch (Exception e ){
            ErrorResponseDTO error = new ErrorResponseDTO("Erro ao excluír produto", HttpStatus.NOT_FOUND.name());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
