package br.com.foursales.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(String descricaoItem, BigDecimal valorItem, BigDecimal valorPago) {
}
