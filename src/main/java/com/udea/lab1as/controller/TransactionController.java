package com.udea.lab1as.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udea.lab1as.dto.TransactionDto;
import com.udea.lab1as.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/transactions", produces = "application/json")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // transferir dinero entre cuentas
    // POST /api/transactions/transfer ---> transferir dinero entre cuentas
    @PostMapping("/transfer")
    // ResponseEntity<?> permite devolver cualquier tipo de respuesta
    public ResponseEntity<TransactionDto> transferMoney(@Valid @RequestBody TransactionDto transactionDto) {
        TransactionDto transaction = transactionService.transferMoney(transactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    // obtener transacciones por numero de cuenta de origen o destino
    // GET /api/transactions/{accountNumber} ---> obtener transacciones por numero
    // de cuenta de origen o destino
    @GetMapping(value = "/{accountNumber}") // Maneja solicitudes GET a /api/transactions
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionDto> transactions = transactionService
                .getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }

}