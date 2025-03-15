package br.com.foursales.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PEDIDO")
    private PedidoEntity pedidoEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUTO")
    private ProdutoEntity produtoEntity;

    @Column
    private BigDecimal valorPago;

    private int qtd;
}