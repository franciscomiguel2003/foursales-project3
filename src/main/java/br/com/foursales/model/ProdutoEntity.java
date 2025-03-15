package br.com.foursales.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column (name="nome")
    private String nome;
    @Column (name="categoria")
    private String categoria;
    @Column(name="preco")
    private BigDecimal preco;
    @Column(name="qtd_estoque")
    private Integer qtdEstoque;

    @OneToMany(mappedBy = "produtoEntity")
    private List<ItemPedidoEntity> itemPedidoEntities;

    public ProdutoEntity(Integer id){
        this.id = id;
    }

    public ProdutoEntity(Integer id, String nome, String categoria, BigDecimal preco, Integer qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }
}
