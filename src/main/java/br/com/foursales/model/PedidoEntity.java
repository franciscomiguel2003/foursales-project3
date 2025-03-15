package br.com.foursales.model;

import br.com.foursales.dto.ItemPedidoDTO;
import br.com.foursales.dto.StatusPedidoEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private Integer idUser;

    @Column(name = "id_status")
    private Integer idStatus;

    @OneToMany(mappedBy = "pedidoEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedidoEntity>  itens = new ArrayList<>();

    @Column
    public BigDecimal valorTotalPedido;

    @Column
    public BigDecimal valorPagoPedido;


}

