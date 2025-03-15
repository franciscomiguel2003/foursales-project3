package br.com.foursales.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponseDTO(Long idPedido, BigDecimal valorTotal,
                                String statusPedido, List<ItemPedidoResponseDTO> listItemPedido){
}
