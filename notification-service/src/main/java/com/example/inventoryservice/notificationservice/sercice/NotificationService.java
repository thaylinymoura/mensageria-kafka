package com.example.inventoryservice.notificationservice.sercice;
import com.example.inventoryservice.notificationservice.dto.InventoryResponseEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService{

    private final SmsService smsService;
    private final EmailService emailService;

    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void consumeInventoryResponse(InventoryResponseEvent event) {
        System.out.println("Notificação recebida para o pedido ID: "+ event.getOrderId());
        smsService.enviarSMS(event.getOrderId(), event.getStatus());
        emailService.enviarEmail(event.getOrderId(), event.getStatus());

    }
}
