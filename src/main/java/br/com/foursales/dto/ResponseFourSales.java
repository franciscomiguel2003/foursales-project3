package br.com.foursales.dto;

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
    private String msg ="";

    public ResponseFourSales(T entidade, String msg) {
        this.response = entidade;
        this.msg = msg;
    }

    public static <T> ResponseEntity getResponse(T entidade, String msg, HttpStatus status) {
        ResponseFourSales ra = new ResponseFourSales(entidade, msg);
        return new ResponseEntity<ResponseFourSales>(ra, status);
    }
}
