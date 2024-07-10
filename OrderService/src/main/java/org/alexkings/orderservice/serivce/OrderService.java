package org.alexkings.orderservice.serivce;

import org.alexkings.orderservice.entity.OrderEntity;

import java.util.UUID;

public interface OrderService {
    OrderEntity createOrder(OrderEntity order);

    OrderEntity updateOrder(UUID orderId, OrderEntity orderDetails);

    void deleteOrder(UUID orderId);

    OrderEntity getOrderById(UUID orderId);
}
