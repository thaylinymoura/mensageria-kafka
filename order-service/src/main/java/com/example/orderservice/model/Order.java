package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Torna a classe uma entidade JPA
@Table(name = "pedidos") // Nome da tabela no banco de dados (pode ser "orders" também)
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado
    private Long id; // ID agora é Long (INT no banco)

    @Column(name = "data_pedido") // Mapeia para a coluna 'data_pedido'
    private LocalDateTime dataPedido;

    @Column(name = "status") // Mapeia para a coluna 'status'
    private String status; // Ex: "PENDENTE", "PROCESSANDO", "CONCLUIDO", "FALHOU"

    @Column(name = "total") // Mapeia para a coluna 'total'
    private BigDecimal total;
}
