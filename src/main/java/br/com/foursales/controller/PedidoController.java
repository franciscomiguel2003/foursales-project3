package br.com.foursales.controller;

import br.com.foursales.dto.*;
import br.com.foursales.model.ItemPedidoEntity;
import br.com.foursales.model.PedidoEntity;
import br.com.foursales.model.UserEntity;
import br.com.foursales.services.PedidoService;
import org.apache.http.protocol.HTTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

            pedido = pedidoService.criarPedido(pedido);
            PedidoEntity pedido2 = pedidoService.buscarPedidosPorId(pedido.getId());
            List<ItemPedidoResponseDTO> listItemDTO = new ArrayList<ItemPedidoResponseDTO>();
            BigDecimal valorTotal = new BigDecimal(0);
            List<ItemPedidoEntity> listItem = pedido2.getItens().stream().toList();

            pedido2.getItens().forEach(i->{
                Hibernate.initialize(i.getProdutoEntity());
                listItemDTO.add(new ItemPedidoResponseDTO(i.getProdutoEntity().getNome(),i.getProdutoEntity().getPreco(),null));
                valorTotal.add(i.getProdutoEntity().getPreco());
            });

            var pedidoResponse = new PedidoResponseDTO(
                    pedido2.getId(),valorTotal,StatusPedidoEnum.valueOf(pedido2.getIdStatus().toString()).toString(), listItemDTO);


            return ResponseFourSales.getResponse(pedidoResponse,
                    "Cadastro efetuado com sucesso", HttpStatus.OK);
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