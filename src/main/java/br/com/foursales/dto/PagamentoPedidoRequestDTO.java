package br.com.foursales.dto;

import java.math.BigDecimal;

public record PagamentoPedidoRequestDTO(Long idPedido, BigDecimal valorPago) {}
