package com.udea.lab1As.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udea.lab1As.dto.TransactionDto;
import com.udea.lab1As.service.TransactionService;


@RestController
@CrossOrigin(origins = "*") // Permitir CORS desde cualquier origen
@RequestMapping( value = "/api/transactions", produces = "application/json")
public class TransactionController {

    @Autowired // inyección de dependencias a través del constructor no es necesario definir el constructor
    private TransactionService transactionService;


    // transferir dinero entre cuentas
    // POST /api/transactions/transfer ---> transferir dinero entre cuentas
    @PostMapping("/transfer")
    // ResponseEntity<?> permite devolver cualquier tipo de respuesta
    public ResponseEntity<?> transferMoney(@RequestBody TransactionDto transactionDto) {
        try {
            TransactionDto transaction = transactionService.transferMoney(transactionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error processing transaction: " + e.getMessage());
        }
    }

    // obtener transacciones por numero de cuenta de origen o destino
    // GET /api/transactions/{accountNumber} ---> obtener transacciones por numero de cuenta de origen o destino
    @GetMapping(value = "/{accountNumber}") // Maneja solicitudes GET a /api/transactions
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionDto> transactions = transactionService
            .getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }

}