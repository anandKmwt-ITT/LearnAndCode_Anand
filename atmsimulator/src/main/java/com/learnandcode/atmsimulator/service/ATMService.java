package com.learnandcode.atmsimulator.service;

import com.learnandcode.atmsimulator.dto.TransactionRequest;
import com.learnandcode.atmsimulator.dto.TransactionResponse;

public interface ATMService {
    TransactionResponse withdraw(TransactionRequest req);
    TransactionResponse deposit(TransactionRequest req);
}
