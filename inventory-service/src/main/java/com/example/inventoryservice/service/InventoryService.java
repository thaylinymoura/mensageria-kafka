package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponseEvent;
import com.example.inventoryservice.dto.OrderEvent;
import com.example.inventoryservice.dto.OrderItemDTO;
import com.example.inventoryservice.model.Produto;
import com.example.inventoryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Para logs
public class InventoryService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate;
    private static final String RESPONSE_TOPIC = "inventory-events";

    @KafkaListener(topics = "orders", groupId = "inventory-group")
    @Transactional // Garante que as operações no banco sejam atômicas
    public void processOrder(OrderEvent orderEvent) {
        log.info("Recebido pedido ID: {} para verificação de estoque.", orderEvent.getId());

        List<Produto> productsToUpdate = new ArrayList<>();
        boolean allItemsInStock = true;

        // 1. Fase de Verificação: Checa todos os produtos antes de alterar o banco
        for (OrderItemDTO item : orderEvent.getItems()) {
            Produto produto = productRepository.findById(item.getProductId()).orElse(null);

            if (produto == null) {
                log.error("Produto com ID {} não encontrado.", item.getProductId());
                sendResponse(orderEvent.getId(), "FAILED", "Produto não encontrado: ID " + item.getProductId());
                return; // Aborta o processamento
            }

            if (produto.getEstoque_quantidade() < item.getQuantity()) {
                log.warn("Estoque insuficiente para o produto: {} (Pedido: {} / Estoque: {})",
                        produto.getNome(), item.getQuantity(), produto.getEstoque_quantidade());
                allItemsInStock = false;
                break; // Interrompe a verificação se um item estiver fora de estoque
            }
            // Temporariamente deduz o estoque para processamento
            produto.setEstoque_quantidade(produto.getEstoque_quantidade() - item.getQuantity());
            productsToUpdate.add(produto);
        }

        // 2. Fase de Confirmação: Se tudo estiver OK, atualiza o estoque e envia sucesso
        if (allItemsInStock) {
            log.info("Todos os itens em estoque para o pedido ID: {}. Atualizando banco de dados.", orderEvent.getId());
            productRepository.saveAll(productsToUpdate); // Salva todas as atualizações de uma vez
            sendResponse(orderEvent.getId(), "SUCCESS", "Pedido processado e estoque reservado com sucesso.");
        } else {
            // Se algo falhou, envia a falha (a transação será revertida, nada será salvo no banco)
            log.error("Falha na reserva de estoque para o pedido ID: {}. Estoque insuficiente.", orderEvent.getId());
            sendResponse(orderEvent.getId(), "FAILED", "Falha na reserva de estoque: um ou mais itens estão indisponíveis.");
        }
    }

    private void sendResponse(Long orderId, String status, String message) {
        InventoryResponseEvent responseEvent = new InventoryResponseEvent(orderId, status, message);
        kafkaTemplate.send(RESPONSE_TOPIC, responseEvent);
        log.info("Resposta enviada ao tópico '{}': {}", RESPONSE_TOPIC, responseEvent);
    }
}
