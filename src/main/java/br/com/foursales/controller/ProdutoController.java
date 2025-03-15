package br.com.foursales.controller;

import br.com.foursales.dto.ErrorResponseDTO;
import br.com.foursales.dto.ProdutoRequestDTO;
import br.com.foursales.model.ProdutoEntity;
import br.com.foursales.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/listarProdutos")
    public List<ProdutoEntity> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @PutMapping("/criarProduto")
    public ResponseEntity criarProduto(@RequestBody @Valid ProdutoRequestDTO prodDTO) {
        ProdutoEntity prod = new ProdutoEntity(null, prodDTO.nome(),prodDTO.categoria(),prodDTO.preco(),prodDTO.qtdEstoque());
        produtoService.criarProduto(prod);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/editaProduto")
    public ResponseEntity editaProduto(@RequestBody @Valid ProdutoRequestDTO prodDTO) {
        ProdutoEntity prod = new ProdutoEntity(prodDTO.id(), prodDTO.nome(),prodDTO.categoria(),prodDTO.preco(),prodDTO.qtdEstoque());
        produtoService.atualizaProduto(prod);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/excluiProduto")
    public ResponseEntity excluiProduto(@RequestParam Integer id) {

        try {
            produtoService.excluiProduto(id);
        }catch (Exception e ){
            ErrorResponseDTO error = new ErrorResponseDTO("Produto não encontrado", HttpStatus.NOT_FOUND.name());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }
}
