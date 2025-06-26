package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        try {
            Order newOrder = orderService.placeOrder(request);
            return ResponseEntity.ok("Pedido recebido com sucesso. ID: " + newOrder.getId());
        } catch (RuntimeException e) {

            System.err.println("Erro ao criar pedido: " + e.getMessage());
            return ResponseEntity.status(400).body("Erro ao processar o pedido: " + e.getMessage());
        } catch (Exception e) {

            System.err.println("Erro interno ao criar pedido: " + e.getMessage());
            return ResponseEntity.status(500).body("Erro interno do servidor ao processar o pedido.");
        }
    }
}
