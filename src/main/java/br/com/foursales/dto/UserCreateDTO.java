package br.com.foursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateDTO(
        @NotBlank
        String username,
        @NotNull
        String password,

        @NotNull
        String nome,

        @NotNull
        String role,

        @NotNull
        String email

) {
}
