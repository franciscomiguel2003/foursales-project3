package br.com.foursales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class ResponseFourSales <T>{

   private T response;
    private String erro="";

    public ResponseFourSales(T entidade, String erro) {
        this.response = entidade;
        this.erro = erro;
    }

    public static <T> ResponseEntity getResponse(T entidade, String erro, HttpStatus status) {
        ResponseFourSales ra = new ResponseFourSales(entidade, erro);
        return new ResponseEntity<ResponseFourSales>(ra, status);
    }
}
