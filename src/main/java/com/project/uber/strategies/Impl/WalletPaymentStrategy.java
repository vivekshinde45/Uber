package com.project.uber.strategies.Impl;

import com.project.uber.entities.Driver;
import com.project.uber.entities.Payment;
import com.project.uber.entities.Rider;
import com.project.uber.entities.enums.PaymentStatus;
import com.project.uber.entities.enums.TransactionMethod;
import com.project.uber.repositories.PaymentRepository;
import com.project.uber.services.PaymentService;
import com.project.uber.services.WalletService;
import com.project.uber.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        // This is wallet to wallet transactions
        // Rider had 232 rs, Driver had 500
        // Ride cost is 100 rs, commission is 30 rs and driver's cut is 70 rs
        // Rider -> Driver
        // 100    -> 500 + (100 - 30) = 570

        // get rider
        Rider rider = payment.getRide().getRider();

        // get driver
        Driver driver = payment.getRide().getDriver();

        // deduct money from Rider's wallet
        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        // calculate the driver's cut
        double driverCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        // TODO: add the platform commission to our account

        // add money to Driver's wallet
        walletService.addMoneyToWallet(driver.getUser(), driverCut, null, payment.getRide(), TransactionMethod.RIDE);

        // update the Payment status to CONFIRMED
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
