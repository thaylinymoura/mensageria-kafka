package com.example.orderservice.kafka;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate; // Injeção do KafkaTemplate configurado
    private static final String TOPIC = "orders"; // Nome do tópico Kafka para pedidos

    /**
     * Envia um objeto Order para o tópico 'orders' no Kafka.
     *
     * @param order O objeto Order a ser enviado.
     */
    public void sendOrder(OrderDTO order) {
        kafkaTemplate.send(TOPIC, order); // Envia a mensagem para o tópico
        System.out.println("Pedido enviado para Kafka: ID do Pedido = " + order.getId() + ", Status = " + order.getStatus()); // Log para acompanhamento
    }


}

