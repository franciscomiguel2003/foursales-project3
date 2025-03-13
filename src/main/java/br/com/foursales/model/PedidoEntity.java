package br.com.foursales.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
    private Long userId;
    private boolean pago;

    @OneToMany(mappedBy = "pedidoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedidoEntity> itens;
}

