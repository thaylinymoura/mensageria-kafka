package com.example.inventoryservice.notificationservice.dto;

import lombok.Data;

// @Data do Lombok gera getters, setters, toString, etc.
@Data
public class InventoryResponseEvent {
    private Long orderId;
    private String status; // "SUCCESS" ou "FAILED"
    private String message;
}
