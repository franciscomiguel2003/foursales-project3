package br.com.foursales.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDocument {
    @org.springframework.data.annotation.Id
    private Long id;
    private String nome;
    private String categoria;
    private double preco;
}