package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import jakarta.persistence.Id;


import java.math.BigDecimal;


@Entity // Indica que esta classe é uma entidade JPA
@Table (name = "produto") // Mapeia para a tabela 'produto' no seu MySQL
@Data // Anotação Lombok para gerar getters, setters, toString, equals e hashCode
@AllArgsConstructor // Anotação Lombok para gerar construtor com todos os argumentos
@NoArgsConstructor // Anotação Lombok para gerar construtor sem argumentos

public class Produto {

    @Id // Marca o campo 'id' como a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Usaremos UUID para identificadores de produto
    private String nome;
    private String descricao;

    @Column(name = "preco")
    private BigDecimal preco;

    @Column(name = "image_url")
    private String image_url;// Corresponde à coluna 'image_url' no banco de dados

    @Column(name = "estoque_quantidade")
    private Integer estoque_quantidade; // Corresponde à coluna 'estoque_quantidade'


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getEstoque_quantidade() {
        return estoque_quantidade;
    }

    public void setEstoque_quantidade(Integer estoque_quantidade) {
        this.estoque_quantidade = estoque_quantidade;
    }
}
