package br.com.foursales.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String nome;
    @Column
    private String categoria;
    @Column
    private BigDecimal preco;
    @Column
    private Integer qtdEstoque;

/*    public ProdutoEntity(Integer id){
        this.id = id;
    }*/
    public ProdutoEntity(Integer id, String nome, String categoria, BigDecimal preco, Integer qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }
}
