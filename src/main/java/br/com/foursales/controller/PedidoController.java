package br.com.foursales.controller;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
import br.com.foursales.dto.*;
import br.com.foursales.model.UserEntity;
import br.com.foursales.services.PedidoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("pedido")
class PedidoController {
    private final PedidoService pedidoService;

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    @PostMapping("/criarPedido")
    public ResponseEntity criarPedido(@RequestBody PedidoRequestDTO pedidoDTO, @AuthenticationPrincipal UserEntity user) {

        try {

            PedidoResponseDTO pedido = pedidoService.criarPedido(user, pedidoDTO.itemPedidoListDTO());

            return ResponseFourSales.getResponse(pedido,"Cadastro efetuado com sucesso", HttpStatus.OK);
        } catch (Exception e ){
            String error = "ERRO CRIAR PEDIDO";
            if(e instanceof FourSalesBusinessException)
                error=e.getMessage();
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PutMapping("/pagar")
    public ResponseEntity pagarPedido(@RequestBody PagamentoPedidoRequestDTO pedidoRequestDTO) {

        try {
            PagamentoPedidoResponseDTO pedidoResponde  = pedidoService.pagarPedido(pedidoRequestDTO.idPedido(), pedidoRequestDTO.valorPago());
            return ResponseFourSales.getResponse(pedidoResponde, pedidoResponde.message(), HttpStatus.OK);

        } catch (Exception e){
            String error = "ERRO AO EFETUAR PAGAMENTO";
            if(e instanceof FourSalesBusinessException)
                error=e.getMessage();
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("/buscarTotalFaturadoMes")
    public ResponseEntity buscarTotalFaturadoMes(String mesAnoReferencia){

         try {
            List<TotalFaturadoMesResponseDTO> listTotalFaturado = pedidoService.buscarTotalFaturadoMes(mesAnoReferencia);
            return ResponseFourSales.getResponse(listTotalFaturado, "Dado retornado com sucesso", HttpStatus.OK);

        } catch (Exception e){
             String error = "Erro ao buscar buscarTotalFaturadoMes";
             logger.error(error, e);
             return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarTicketMedioUsuario")
    public ResponseEntity buscarTicketMedioUsuario(){


        try {
            List<TicketMedioUsuarioResponseDTO> ListTicketMedio = pedidoService.buscarTicketMedioUsuario();
             return ResponseFourSales.getResponse(ListTicketMedio, "Dado retornado com sucesso", HttpStatus.OK);

        } catch (Exception e){
            String error = "Erro ao buscar buscarTicketMedioUsuario";
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/buscarTop5UsuariosCompras")
    public ResponseEntity buscarTop5UsuariosCompras(){


        try {
            List<Top5UsuariosComprasResponseDTO> listTop5Usuarios = pedidoService.buscarTop5UsuariosCompras();
            return ResponseFourSales.getResponse(listTop5Usuarios, "Dado retornado com sucesso", HttpStatus.OK);

        } catch (Exception e){
            String error = "buscarTop5UsuariosCompras";
            logger.error(error, e);
            return ResponseFourSales.getResponse(null, error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}