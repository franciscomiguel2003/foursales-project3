package br.com.foursales.dto;

public enum StatusPedidoEnum {

    PENDENTE(1),
    PAGO(2),
    CANDELADO(3);

    Integer idStatus;

    private StatusPedidoEnum(Integer idStatus){
        this.idStatus = idStatus;
    }

    public Integer getIdStatus(){
        return this.idStatus;
    }


}
