package br.com.foursales.controller;

import br.com.foursales.model.PedidoEntity;
import br.com.foursales.services.PedidoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/pedidos")
class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/criar")
    public PedidoEntity criarPedido(@RequestBody PedidoEntity pedidoEntity) {
        return pedidoService.criarPedido(pedidoEntity);
    }

    @PostMapping("/pagar/{id}")
    public void pagarPedido(@PathVariable Long id) {
        pedidoService.pagarPedido(id);
    }

}