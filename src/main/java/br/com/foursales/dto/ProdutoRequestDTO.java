package br.com.foursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDTO(

        Integer id,
        @NotBlank
        String nome,
        @NotBlank
        String categoria,
        @NotNull
        BigDecimal preco,

        int qtdEstoque

) {
        public static class MelhoresClientesDTO {
        }
}
