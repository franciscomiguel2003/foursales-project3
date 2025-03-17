package br.com.foursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(

        @NotBlank
        String username,

        @NotBlank
        String perfil

) {
}
