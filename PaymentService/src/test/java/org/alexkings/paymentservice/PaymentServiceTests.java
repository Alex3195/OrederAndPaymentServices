package org.alexkings.paymentservice;


import jakarta.transaction.Transactional;
import org.alexkings.paymentservice.entity.Payment;
import org.alexkings.paymentservice.repository.PaymentRepository;
import org.alexkings.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "order-created", "payment-created", "payment-confirmed" })
public class PaymentServiceTests {

    @Autowired
    private PaymentService paymentService;

    @Test
    @Transactional
    @Rollback
    public void testCreatePayment() {
        Payment payment = new Payment();
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(100.0);

        Payment createdPayment = paymentService.createPayment(payment);

        assertNotNull(createdPayment.getId());
        assertEquals("CREATED", createdPayment.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    public void testConfirmPayment() {
        Payment payment = new Payment();
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(100.0);
        Payment createdPayment = paymentService.createPayment(payment);

        Payment confirmedPayment = paymentService.confirmPayment(createdPayment.getId());

        assertNotNull(confirmedPayment.getId());
        assertEquals("CONFIRMED", confirmedPayment.getStatus());
    }
}
