package org.alexkings.orderservice.listener;

import org.alexkings.orderservice.entity.OrderEntity;
import org.alexkings.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "payment-confirmed", groupId = "order-service")
    public void handlePaymentConfirmed(String paymentId) {
        // Handle the event of payment confirmation (e.g., update order status)
        // This is a simple example, you might want to add more complex logic here
        OrderEntity order = orderRepository.findById(UUID.fromString(paymentId)).orElseThrow();
        order.setStatus("COMPLETED");
        orderRepository.save(order);
    }
}
