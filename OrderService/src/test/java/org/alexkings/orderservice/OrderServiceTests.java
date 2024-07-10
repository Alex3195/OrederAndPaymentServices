package org.alexkings.orderservice;

import jakarta.transaction.Transactional;
import org.alexkings.orderservice.entity.OrderEntity;
import org.alexkings.orderservice.repository.OrderRepository;
import org.alexkings.orderservice.serivce.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"order-created", "payment-confirmed"})
public class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    @Rollback
    public void testCreateOrder() {
        OrderEntity order = new OrderEntity();
        order.setProduct("Test Product");
        order.setQuantity(1);

        OrderEntity createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder.getId());
        assertEquals("CREATED", createdOrder.getStatus());
    }
}