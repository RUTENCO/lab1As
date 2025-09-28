package com.udea.lab1As.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udea.lab1As.dto.TransactionDto;
import com.udea.lab1As.entity.Transaction;
import com.udea.lab1As.repository.CustomerRepository;
import com.udea.lab1As.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional // Si algo falla, TODO se revierte (rollback)
    public TransactionDto transferMoney(TransactionDto transactionDto) {
        // Lógica para transferir dinero entre cuentas

        // validar que los  numeros de cuenta no sean nulos
        if (transactionDto.getSenderAccountNumber() == null || 
            transactionDto.getReceiverAccountNumber() == null) {
            throw new IllegalArgumentException("Account numbers cannot be void");
        }

        // buscar los clientes por su numero de cuenta
        var sender = customerRepository.findByAccountNumber(transactionDto.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        var receiver = customerRepository.findByAccountNumber(transactionDto.getReceiverAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // validar que el monto de la transacción no sea nulo
        if (transactionDto.getAmount() == null) {
            throw new IllegalArgumentException("Transaction amount cannot be void");
        }

        // validar que el saldo del remitente sea suficiente
        // compareTo devuelve un valor negativo si el primer valor es menor que el segundo
        // cero si son iguales y un valor positivo si el primer valor es mayor que el segundo
        if (sender.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // actualizar los saldos de las cuentas
        sender.setBalance(sender.getBalance() - transactionDto.getAmount()); // restar el monto de la transacción al saldo del remitente
        receiver.setBalance(receiver.getBalance() + transactionDto.getAmount()); // sumar el monto de la transacción al saldo del receptor

        // guardar la transacción en la base de datos
        customerRepository.save(sender);
        customerRepository.save(receiver);

        // crear y guardar la transacción
        var transaction =  new Transaction();
        transaction.setSenderAccountNumber(sender.getAccountNumber());
        transaction.setReceiverAccountNumber(receiver.getAccountNumber());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate()); // GREGAR LA FECHA
        transactionRepository.save(transaction);

        // retornar la transacción como DTO
        transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setSenderAccountNumber(transaction.getSenderAccountNumber());
        transactionDto.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionDate(transaction.getTransactionDate()); // AGREGAR LA FECHA

        return transactionDto;
    }

    // obtener la lista de todas las transacciones por numero de cuenta
    public List<TransactionDto> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository
                .findBySenderAccountNumberOrReceiverAccountNumber(accountNumber, accountNumber);
        return transactions.stream().map(transaction -> {
            var dto = new TransactionDto();
            dto.setId(transaction.getId());
            dto.setSenderAccountNumber(transaction.getSenderAccountNumber());
            dto.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
            dto.setAmount(transaction.getAmount());
            dto.setTransactionDate(transaction.getTransactionDate()); //  AGREGAR LA FECHA
            return dto;
        }).collect(Collectors.toList());
    }

}
