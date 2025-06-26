package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "pedidos")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "status")
    private String status;

    @Column(name = "total")
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> items = new ArrayList<>();
}
