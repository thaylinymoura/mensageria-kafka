package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Marca esta classe como um controlador REST
@RequestMapping("/orders") // Define o caminho base para os endpoints
@RequiredArgsConstructor // Gera um construtor para injeção de dependência do OrderService
@CrossOrigin(origins = "*") // Permite requisições CORS de qualquer origem para o front-end
public class OrderController {

    private final OrderService orderService; // Injeção do serviço de Pedido

    /**
     * Endpoint para criar um novo pedido.
     * Recebe um OrderRequest via POST em /orders.
     * @param request O DTO OrderRequest contendo os itens do pedido.
     * @return ResponseEntity indicando o sucesso ou falha da operação.
     */
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        try {
            Order newOrder = orderService.placeOrder(request); // Delega a lógica de negócio ao OrderService
            return ResponseEntity.ok("Pedido recebido com sucesso. ID: " + newOrder.getId());
        } catch (RuntimeException e) {
            // Captura exceções de lógica de negócio (ex: produto não encontrado, estoque insuficiente)
            System.err.println("Erro ao criar pedido: " + e.getMessage());
            return ResponseEntity.status(400).body("Erro ao processar o pedido: " + e.getMessage()); // Retorna 400 Bad Request para erros de cliente
        } catch (Exception e) {
            // Captura outras exceções inesperadas
            System.err.println("Erro interno ao criar pedido: " + e.getMessage());
            return ResponseEntity.status(500).body("Erro interno do servidor ao processar o pedido."); // Retorna 500 Internal Server Error
        }
    }
}
