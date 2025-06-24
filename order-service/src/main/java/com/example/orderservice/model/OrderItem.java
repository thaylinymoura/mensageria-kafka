package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


import java.math.BigDecimal;
import java.util.UUID;


@Entity // Torna a classe uma entidade JPA
@Table(name = "itens_pedido") // Nome da tabela no banco de dados
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado
    private Long id; // ID agora é Long (INT no banco)

    @ManyToOne // Indica que muitos OrderItems podem pertencer a um único Order
    @JoinColumn(name = "pedido_id") // Define a coluna de chave estrangeira que faz a ligação com a tabela 'pedidos'
    private Order order; // Referência ao objeto Order ao qual este item pertence

    @Column(name = "produto_id") // Mapeia para a coluna 'produto_id'
    private Long produtoId; // O ID do produto real que está sendo comprado (agora Long)

    @Column(name = "quantidade") // Mapeia para a coluna 'quantidade'
    private Integer quantidade; // A quantidade deste produto no pedido

    @Column(name = "preco_unitario") // Mapeia para a coluna 'preco_unitario'
    private BigDecimal precoUnitario; // O preço unitário do produto no momento da compra
}
