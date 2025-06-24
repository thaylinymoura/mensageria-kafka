package com.example.inventoryservice.dto;
import lombok.Data;

import java.util.List;

@Data
public class OrderEvent {
    private Long id;
    private List<OrderItemDTO> items;
}
