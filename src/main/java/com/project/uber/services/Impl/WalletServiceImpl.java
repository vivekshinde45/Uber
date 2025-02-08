package com.project.uber.services.Impl;

import com.project.uber.entities.Ride;
import com.project.uber.entities.User;
import com.project.uber.entities.Wallet;
import com.project.uber.entities.WalletTransaction;
import com.project.uber.entities.enums.TransactionMethod;
import com.project.uber.entities.enums.TransactionType;
import com.project.uber.exceptions.ResourceNotFoundException;
import com.project.uber.repositories.WalletRepository;
import com.project.uber.services.WalletService;
import com.project.uber.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user,
                                   Double amount,
                                   String transactionId,
                                   Ride ride,
                                   TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .balance(amount)
                .transactionMethod(transactionMethod)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user,
                                        Double amount,
                                        String transactionId,
                                        Ride ride,
                                        TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .balance(amount)
                .transactionMethod(transactionMethod)
                .build();

//        walletTransactionService.createNewWalletTransaction(walletTransaction);
        wallet.getTransactions().add(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(
                () -> new ResourceNotFoundException("Wallet not found with ID: " + walletId)
        );
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("Wallet not found for user with ID: " + user.getId())
        );
    }
}
