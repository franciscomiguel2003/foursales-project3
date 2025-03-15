package br.com.foursales.model;

import br.com.foursales.dto.ItemPedidoDTO;
import br.com.foursales.dto.StatusPedidoEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@AllArgsConstructor
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer idUser;

    @Column(name = "id_status")
    private Integer idStatus;

    @OneToMany(mappedBy = "pedidoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<ItemPedidoEntity> itens;

    @Column
    public BigDecimal valorTotalPedido;

    @Column
    public BigDecimal valorPagoPedido;

    public PedidoEntity(Integer idUser, StatusPedidoEnum statusPedido, List<ItemPedidoDTO> itemPedidoListDTO){
        this.idUser = idUser;
        this.idStatus = statusPedido.getIdStatus();

        if(itemPedidoListDTO != null && !itemPedidoListDTO.isEmpty()) {
            if(itens == null)
                itens = new HashSet<>();

            itemPedidoListDTO.forEach(i -> {
                    ItemPedidoEntity itemPedidoE = new ItemPedidoEntity();
                    itemPedidoE.setProdutoEntity(new ProdutoEntity(i.idProduto(),null, null, null, null));
                    itemPedidoE.setQtd(itemPedidoE.getQtd());
                    itemPedidoE.setPedidoEntity(this);
                    itens.add(itemPedidoE);
                }
            );

        }


    }

    public PedidoEntity(StatusPedidoEnum statusPedido, Long id){
        this.idStatus = statusPedido.getIdStatus();
        this.id = id;
    }


}

