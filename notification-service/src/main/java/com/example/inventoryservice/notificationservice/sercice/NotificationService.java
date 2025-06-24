import com.example.inventoryservice.notificationservice.dto.InventoryResponseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j // Anotação do Lombok para logging fácil
public class NotificationService{

    /**
     * Consome eventos do tópico 'inventory-events'.
     * Este método é ativado sempre que uma nova mensagem chega.
     * @param event O evento de resposta do serviço de inventário.
     */
    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void consumeInventoryResponse(InventoryResponseEvent event) {
        log.info("Notificação recebida para o pedido ID: {}", event.getOrderId());

        // Simula o envio de uma notificação (e-mail, SMS, etc.)
        // com base no status do evento.
        System.out.println("================= NOTIFICAÇÃO =================");
        System.out.println("Para Pedido ID: " + event.getOrderId());

        if ("SUCCESS".equals(event.getStatus())) {
            System.out.println("Status: SUCESSO");
            System.out.println("Mensagem: Olá! " + event.getMessage());
            // Aqui você colocaria a lógica real para enviar um e-mail de sucesso.
        } else {
            System.out.println("Status: FALHA");
            System.out.println("Mensagem: Olá! " + event.getMessage());
            // Aqui você colocaria a lógica para notificar o cliente sobre a falha.
        }
        System.out.println("==============================================");
    }
}
