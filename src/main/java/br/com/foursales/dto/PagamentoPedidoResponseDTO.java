package br.com.foursales.dto;

import br.com.foursales.model.PedidoEntity;

public record PagamentoPedidoResponseDTO(PedidoResponseDTO pedido, String message) {
}
