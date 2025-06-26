package com.example.orderservice.kafka;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private static final String TOPIC = "orders";

    public void sendOrder(OrderDTO order) {
        kafkaTemplate.send(TOPIC, order);
        System.out.println("Pedido enviado para Kafka: ID do Pedido = " + order.getId() + ", Status = " + order.getStatus());
    }


}

