package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Table(name = "itens_pedido")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Order order;

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;
}
