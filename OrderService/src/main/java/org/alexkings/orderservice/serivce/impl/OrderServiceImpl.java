package org.alexkings.orderservice.serivce.impl;

import jakarta.transaction.Transactional;
import org.alexkings.orderservice.entity.OrderEntity;
import org.alexkings.orderservice.repository.OrderRepository;
import org.alexkings.orderservice.serivce.OrderService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Override
    public OrderEntity createOrder(OrderEntity order) {
        order.setStatus("CREATED");
        OrderEntity savedOrder = orderRepository.save(order);
        sendOrderCreatedMessage(savedOrder.getId().toString(), "Order created");
        return savedOrder;
    }

    @Transactional
    @Override
    public OrderEntity updateOrder(UUID orderId, OrderEntity orderDetails) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        order.setProduct(orderDetails.getProduct());
        order.setQuantity(orderDetails.getQuantity());
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderEntity getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public void sendOrderCreatedMessage(String orderId, String message) {
        kafkaTemplate.send("order-created", orderId, message);
    }
}
