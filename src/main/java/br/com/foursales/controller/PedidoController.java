package br.com.foursales.controller;

import br.com.foursales.dto.*;
import br.com.foursales.model.PedidoEntity;
import br.com.foursales.model.UserEntity;
import br.com.foursales.services.PedidoService;
import org.apache.http.protocol.HTTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedido")
class PedidoController {
    private final PedidoService pedidoService;

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    @PutMapping("/criarPedido")
    public ResponseEntity criarPedido(@RequestBody PedidoRequestDTO pedidoDTO, @AuthenticationPrincipal UserEntity user) {

        try {
            PedidoEntity pedido = new PedidoEntity(user.getId().intValue(), StatusPedidoEnum.PENDENTE, pedidoDTO.itemPedidoListDTO());
            return ResponseFourSales.getResponse(pedidoService.criarPedido(pedido), null, HttpStatus.OK);
        } catch (Exception e ){
            String error = "ERRO CRIAR PEDIDO";
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/pagar/{id}")
    public ResponseEntity pagarPedido(@RequestBody PagamentoPedidoRequestDTO pedidoRequestDTO) {

        try {
            PedidoEntity pedidoResponde  = pedidoService.pagarPedido(pedidoRequestDTO.idPedido(), pedidoRequestDTO.valorPago());
            return ResponseFourSales.getResponse(pedidoResponde, null, HttpStatus.OK);
        } catch (Exception e ){
            String error = "ERRO AO EFETUAR PAGAMENTO";
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}