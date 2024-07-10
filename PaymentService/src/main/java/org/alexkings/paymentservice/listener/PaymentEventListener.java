package org.alexkings.paymentservice.listener;

import org.alexkings.paymentservice.entity.Payment;
import org.alexkings.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentEventListener {

    private final PaymentRepository paymentRepository;

    public PaymentEventListener(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "payment-service-group")
    public void listenOrderMessages(String orderId, String message) {
        // Process the received order message
        System.out.println("Received order message - OrderId: " + orderId + ", Message: " + message);
    }

    @KafkaListener(topics = "order-created", groupId = "payment-service")
    public void handleOrderCreated(String orderId) {
        // Handle the event of order creation (e.g., initiate payment creation)
        // This is a simple example, you might want to add more complex logic here
        Payment payment = new Payment();
        payment.setOrderId(UUID.fromString(orderId));
        payment.setAmount(100.0);  // Example amount, you would typically get this from the order details
        payment.setStatus("PENDING");
        paymentRepository.save(payment);
    }
}
