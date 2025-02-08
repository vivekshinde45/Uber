package com.project.uber.strategies;

import com.project.uber.entities.Payment;

public interface PaymentStrategy {
    double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
