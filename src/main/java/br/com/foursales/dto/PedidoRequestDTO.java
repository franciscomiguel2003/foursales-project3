package br.com.foursales.dto;

import java.util.List;

public record PedidoRequestDTO (List<ItemPedidoDTO> itemPedidoListDTO) {
}
