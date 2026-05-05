package com.udea.lab1as.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udea.lab1as.dto.TransactionDto;
import com.udea.lab1as.entity.Customer;
import com.udea.lab1as.entity.Transaction;
import com.udea.lab1as.exception.CustomerNotFoundException;
import com.udea.lab1as.exception.InsufficientFundsException;
import com.udea.lab1as.exception.InvalidTransactionException;
import com.udea.lab1as.mapper.TransactionMapper;
import com.udea.lab1as.repository.CustomerRepository;
import com.udea.lab1as.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private final Customer sender = createCustomer(1L, "John", "Doe", "ACC123", 1000.0);
    private final Customer receiver = createCustomer(2L, "Jane", "Smith", "ACC456", 200.0);
    private final TransactionDto transactionDto = createTransactionDto(null, "ACC123", "ACC456", 250.0,
            LocalDate.of(2026, 5, 3));

    private static Customer createCustomer(Long id, String firstName, String lastName, String accountNumber,
            Double balance) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAccountNumber(accountNumber);
        customer.setBalance(balance);
        return customer;
    }

    private static TransactionDto createTransactionDto(Long id, String senderAccountNumber,
            String receiverAccountNumber, Double amount, LocalDate transactionDate) {
        TransactionDto dto = new TransactionDto();
        dto.setId(id);
        dto.setSenderAccountNumber(senderAccountNumber);
        dto.setReceiverAccountNumber(receiverAccountNumber);
        dto.setAmount(amount);
        dto.setTransactionDate(transactionDate);
        return dto;
    }

    @Test
    void transferMoney_whenValid_shouldUpdateBalancesAndPersistTransaction() {
        Transaction mappedTransaction = new Transaction();
        mappedTransaction.setAmount(250.0);
        mappedTransaction.setTransactionDate(transactionDto.getTransactionDate());

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(10L);
        savedTransaction.setSenderAccountNumber("ACC123");
        savedTransaction.setReceiverAccountNumber("ACC456");
        savedTransaction.setAmount(250.0);
        savedTransaction.setTransactionDate(transactionDto.getTransactionDate());

        TransactionDto savedDto = new TransactionDto(10L, "ACC123", "ACC456", 250.0,
                transactionDto.getTransactionDate());

        when(customerRepository.findByAccountNumber("ACC123")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("ACC456")).thenReturn(Optional.of(receiver));
        when(transactionMapper.toEntity(transactionDto)).thenReturn(mappedTransaction);
        when(transactionRepository.save(mappedTransaction)).thenReturn(savedTransaction);
        when(transactionMapper.toDto(savedTransaction)).thenReturn(savedDto);

        var result = transactionService.transferMoney(transactionDto);

        assertEquals(750.0, sender.getBalance());
        assertEquals(450.0, receiver.getBalance());
        assertEquals(10L, result.getId());
        assertEquals("ACC123", result.getSenderAccountNumber());
        assertEquals("ACC456", result.getReceiverAccountNumber());

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        assertEquals("ACC123", transactionCaptor.getValue().getSenderAccountNumber());
        assertEquals("ACC456", transactionCaptor.getValue().getReceiverAccountNumber());
        verify(customerRepository).save(sender);
        verify(customerRepository).save(receiver);
    }

    @Test
    void transferMoney_whenSenderAccountMissing_shouldThrowInvalidTransaction() {
        transactionDto.setSenderAccountNumber(null);

        var exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Account numbers cannot be void", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_whenReceiverAccountMissing_shouldThrowInvalidTransaction() {
        transactionDto.setReceiverAccountNumber(null);

        var exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Account numbers cannot be void", exception.getMessage());
    }

    @Test
    void transferMoney_whenSameAccounts_shouldThrowInvalidTransaction() {
        transactionDto.setReceiverAccountNumber("ACC123");

        var exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Sender and receiver accounts must be different", exception.getMessage());
    }

    @Test
    void transferMoney_whenAmountIsNull_shouldThrowInvalidTransaction() {
        transactionDto.setAmount(null);

        var exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Transaction amount must be greater than zero", exception.getMessage());
    }

    @Test
    void transferMoney_whenAmountIsZero_shouldThrowInvalidTransaction() {
        transactionDto.setAmount(0.0);

        var exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Transaction amount must be greater than zero", exception.getMessage());
    }

    @Test
    void transferMoney_whenDateIsMissing_shouldThrowInvalidTransaction() {
        transactionDto.setTransactionDate(null);

        var exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Transaction date is required", exception.getMessage());
    }

    @Test
    void transferMoney_whenSenderNotFound_shouldThrowCustomerNotFound() {
        when(customerRepository.findByAccountNumber("ACC123")).thenReturn(Optional.empty());

        var exception = assertThrows(CustomerNotFoundException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Sender account not found", exception.getMessage());
    }

    @Test
    void transferMoney_whenReceiverNotFound_shouldThrowCustomerNotFound() {
        when(customerRepository.findByAccountNumber("ACC123")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("ACC456")).thenReturn(Optional.empty());

        var exception = assertThrows(CustomerNotFoundException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Receiver account not found", exception.getMessage());
    }

    @Test
    void transferMoney_whenInsufficientFunds_shouldThrowInsufficientFunds() {
        sender.setBalance(100.0);

        when(customerRepository.findByAccountNumber("ACC123")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("ACC456")).thenReturn(Optional.of(receiver));

        var exception = assertThrows(InsufficientFundsException.class,
                () -> transactionService.transferMoney(transactionDto));

        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    void getTransactionsByAccountNumber_shouldMapAllResults() {
        Transaction tx1 = new Transaction();
        tx1.setId(1L);
        tx1.setSenderAccountNumber("ACC123");
        tx1.setReceiverAccountNumber("ACC456");
        tx1.setAmount(100.0);
        tx1.setTransactionDate(LocalDate.of(2026, 5, 1));

        Transaction tx2 = new Transaction();
        tx2.setId(2L);
        tx2.setSenderAccountNumber("ACC789");
        tx2.setReceiverAccountNumber("ACC123");
        tx2.setAmount(50.0);
        tx2.setTransactionDate(LocalDate.of(2026, 5, 2));

        TransactionDto dto1 = new TransactionDto(1L, "ACC123", "ACC456", 100.0, LocalDate.of(2026, 5, 1));
        TransactionDto dto2 = new TransactionDto(2L, "ACC789", "ACC123", 50.0, LocalDate.of(2026, 5, 2));

        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber("ACC123", "ACC123"))
                .thenReturn(List.of(tx1, tx2));
        when(transactionMapper.toDto(tx1)).thenReturn(dto1);
        when(transactionMapper.toDto(tx2)).thenReturn(dto2);

        var result = transactionService.getTransactionsByAccountNumber("ACC123");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getTransactionsByAccountNumber_whenNoResults_shouldReturnEmptyList() {
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber("EMPTY", "EMPTY"))
                .thenReturn(List.of());

        var result = transactionService.getTransactionsByAccountNumber("EMPTY");

        assertEquals(0, result.size());
    }
}