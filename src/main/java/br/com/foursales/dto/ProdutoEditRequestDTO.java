package br.com.foursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoEditRequestDTO(

        Integer id,
        String nome,
        String categoria,
        BigDecimal preco,
        int qtdEstoque

) {
}
