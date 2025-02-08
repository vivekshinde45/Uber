package com.project.uber.strategies.Impl;

import com.project.uber.entities.Driver;
import com.project.uber.entities.Payment;
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
public class CashPaymentStrategy implements PaymentStrategy {
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        // This is related to Cash on delivery payment, where Driver receives the payment from rider via CASH
        // for example: Ride fare is 100 rs, Rider -> Driver (CASH),
        // get the commission from driver which is 30% i.e. 30 rs

        // get the driver
        Driver driver = payment.getRide().getDriver();

        // calculate the platform commission
        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        // deduct it from driver's wallet
        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, payment.getRide(), TransactionMethod.RIDE);

        // update the payment status to CONFIRMED
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
