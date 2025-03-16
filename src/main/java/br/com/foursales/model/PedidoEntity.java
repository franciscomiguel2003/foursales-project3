package br.com.foursales.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private UserEntity user;

    @Column(name = "id_status")
    private Integer idStatus;

    @JsonBackReference
    @OneToMany(mappedBy = "pedidoEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedidoEntity>  itens = new ArrayList<>();

    @Column
    public BigDecimal valorTotalPago;

    @Column
    public BigDecimal valorTotal;


}

