package com.project.uber.services.Impl;

import com.project.uber.entities.Payment;
import com.project.uber.entities.Ride;
import com.project.uber.entities.enums.PaymentStatus;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.repositories.PaymentRepository;
import com.project.uber.services.PaymentService;
import com.project.uber.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found for Ride with ID: " + ride.getId())
        );
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(ride.getPaymentMethod())
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
