package com.learnandcode.atmsimulator.controller;

import com.learnandcode.atmsimulator.dto.TransactionRequest;
import com.learnandcode.atmsimulator.dto.TransactionResponse;
import com.learnandcode.atmsimulator.service.ATMService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/atm")
public class ATMController {

    @Autowired
    private ATMService atmService;

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = atmService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = atmService.deposit(request);
        return ResponseEntity.ok(response);
    }
}