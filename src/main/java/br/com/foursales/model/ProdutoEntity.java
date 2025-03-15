package br.com.foursales.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String categoria;
    private Double preco;
    @Column(name="qtd_estoque")
    private Integer qtdEstoque;

    public ProdutoEntity(Integer id){
        this.id = id;
    }
    public ProdutoEntity(Integer id, String nome, String categoria, Double preco, Integer qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }
}
