package com.learnandcode.atmsimulator.service;

import com.learnandcode.atmsimulator.dto.TransactionRequest;
import com.learnandcode.atmsimulator.dto.TransactionResponse;
import com.learnandcode.atmsimulator.model.ATM;
import com.learnandcode.atmsimulator.model.UserAccount;
import com.learnandcode.atmsimulator.repository.ATMRepository;
import com.learnandcode.atmsimulator.repository.UserAccountRepository;
import com.learnandcode.atmsimulator.service.helper.ServerValidator;
import com.learnandcode.atmsimulator.service.helper.TransactionValidator;
import com.learnandcode.atmsimulator.service.helper.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ATMServiceImpl implements ATMService {

    private final UserAccountRepository userRepository;
    private final ATMRepository atmRepository;

    private final ServerValidator serverValidator;
    private final UserValidator userValidator;
    private final TransactionValidator transactionValidator;

    @Override
    public TransactionResponse withdraw(TransactionRequest req) {
        serverValidator.checkServerConnection();

        UserAccount user = userValidator.validateUserAndPin(req.getCardNumber(), req.getPin());
        ATM atm = atmRepository.findById(1).orElseThrow();

        transactionValidator.validateWithdrawal(user, atm, req.getAmount());

        user.setBalance(user.getBalance() - req.getAmount());
        user.setDailyWithdrawn(user.getDailyWithdrawn() + req.getAmount());
        atm.setAvailableCash(atm.getAvailableCash() - req.getAmount());

        userRepository.save(user);
        atmRepository.save(atm);

        return new TransactionResponse("Withdrawal successful", user.getBalance(), atm.getAvailableCash());
    }

    @Override
    public TransactionResponse deposit(TransactionRequest req) {
        UserAccount user = userValidator.validateUserAndPin(req.getCardNumber(), req.getPin());
        ATM atm = atmRepository.findById(1).orElseThrow();

        user.setBalance(user.getBalance() + req.getAmount());
        atm.setAvailableCash(atm.getAvailableCash() + req.getAmount());

        userRepository.save(user);
        atmRepository.save(atm);

        return new TransactionResponse("Deposit successful", user.getBalance(), atm.getAvailableCash());
    }
}
