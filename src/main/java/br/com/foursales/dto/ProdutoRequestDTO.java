package br.com.foursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record ProdutoRequestDTO(

        Integer id,
        @NotBlank
        String nome,
        @NotBlank
        String categoria,
        @NotNull
        Double preco,

        int qtdEstoque

) {}
