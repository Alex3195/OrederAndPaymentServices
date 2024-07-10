package org.alexkings.paymentservice.controller;

import org.alexkings.paymentservice.entity.Payment;
import org.alexkings.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/{paymentId}/confirm")
    public ResponseEntity<Payment> confirmPayment(@PathVariable UUID paymentId) {
        Payment confirmedPayment = paymentService.confirmPayment(paymentId);
        return ResponseEntity.ok(confirmedPayment);
    }
}