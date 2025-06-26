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

    private Long id;
    private LocalDateTime dataPedido;
    private String status;
    private BigDecimal total;

    private List<OrderItemDTO>items;
}
