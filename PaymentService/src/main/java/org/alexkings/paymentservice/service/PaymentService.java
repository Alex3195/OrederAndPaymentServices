package org.alexkings.paymentservice.service;

import org.alexkings.paymentservice.entity.Payment;

import java.util.UUID;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment confirmPayment(UUID paymentId);
}
