package com.example.orderservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Setter
@Getter
public class OrderDTO {

    private Long id; // ID agora é Long (INT no banco)
    private LocalDateTime dataPedido;
    private String status; // Ex: "PENDENTE", "PROCESSANDO", "CONCLUIDO", "FALHOU"
    private BigDecimal total;

    private List<OrderItemDTO>items;
}
