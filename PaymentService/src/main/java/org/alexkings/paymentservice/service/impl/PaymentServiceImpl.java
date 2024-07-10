package org.alexkings.paymentservice.service.impl;

import jakarta.transaction.Transactional;
import org.alexkings.paymentservice.entity.Payment;
import org.alexkings.paymentservice.repository.PaymentRepository;
import org.alexkings.paymentservice.service.PaymentService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @Override
    public Payment createPayment(Payment payment) {
        payment.setStatus("CREATED");
        Payment savedPayment = paymentRepository.save(payment);
        kafkaTemplate.send("payment-created", savedPayment.getId().toString());
        return savedPayment;
    }

    @Transactional
    @Override
    public Payment confirmPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        payment.setStatus("CONFIRMED");
        Payment updatedPayment = paymentRepository.save(payment);
        kafkaTemplate.send("payment-confirmed", updatedPayment.getId().toString());
        return updatedPayment;
    }
}
