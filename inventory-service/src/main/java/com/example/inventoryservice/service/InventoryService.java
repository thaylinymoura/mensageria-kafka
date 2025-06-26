package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponseEvent;
import com.example.inventoryservice.dto.OrderEvent;
import com.example.inventoryservice.dto.OrderItemDTO;
import com.example.inventoryservice.model.Order;
import com.example.inventoryservice.model.Produto;
import com.example.inventoryservice.repository.OrderRepository;
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

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String,InventoryResponseEvent> kafkaTemplate;
    private static final String RESPONSE_TOPIC = "inventory-events";

    @KafkaListener(topics = "orders", groupId = "inventory-group")
    @Transactional
    public void processOrder(OrderEvent orderEvent) {
        System.out.println("Recebido pedido ID: {} para verificação de estoque."+ orderEvent.getId());

        sendMessageTopic(orderEvent.getId(), "PENDENTE");



        for (OrderItemDTO item : orderEvent.getItems()) {
            Produto produto = productRepository.findById(item.getProductId()).orElse(null);

            if (produto == null) {
                System.out.println("Produto com ID {} não encontrado."+ item.getProductId());
                return;
            }


            System.out.println("Estoque atual" + produto.getEstoque_quantidade());
            produto.setEstoque_quantidade(produto.getEstoque_quantidade() - item.getQuantity());
            System.out.println("Quantidade itens" + item.getQuantity());
            System.out.println("Estoque alterado" + produto.getEstoque_quantidade());


            System.out.println("Atualizando banco de dados."+ orderEvent.getId());
            productRepository.save(produto);
            orderRepository.save(new Order(orderEvent.getId(), "SUCESSO"));

        }

        sendMessageTopic(orderEvent.getId(), "SUCESSO");

    }

    private void sendMessageTopic(Long orderId, String status) {
        InventoryResponseEvent responseEvent = new InventoryResponseEvent(orderId,status);
        kafkaTemplate.send(RESPONSE_TOPIC,responseEvent);
        System.out.println("Resposta enviada ao tópico"+ RESPONSE_TOPIC+ responseEvent);
    }
}
