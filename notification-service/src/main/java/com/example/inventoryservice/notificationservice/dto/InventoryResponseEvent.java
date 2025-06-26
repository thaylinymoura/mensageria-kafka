package com.example.inventoryservice.notificationservice.dto;

import lombok.Data;

@Data
public class InventoryResponseEvent {
    private Long orderId;
    private String status;
}
