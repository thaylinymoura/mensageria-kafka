package com.example.inventoryservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@Setter
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private BigDecimal preco;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "estoque_quantidade")
    private Integer estoque_quantidade;
}
