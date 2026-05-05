package com.udea.lab1as.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TransactionEntityTest {

    @Test
    void defaultConstructorAndSetters_shouldPopulateFields() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSenderAccountNumber("ACC123");
        transaction.setReceiverAccountNumber("ACC456");
        transaction.setAmount(150.0);
        transaction.setTransactionDate(LocalDate.of(2026, 5, 3));

        assertEquals(1L, transaction.getId());
        assertEquals("ACC123", transaction.getSenderAccountNumber());
        assertEquals("ACC456", transaction.getReceiverAccountNumber());
        assertEquals(150.0, transaction.getAmount());
        assertEquals(LocalDate.of(2026, 5, 3), transaction.getTransactionDate());
        assertNotNull(transaction.toString());
    }

    @Test
    void jsonCreatorConstructor_shouldPopulateFields() {
        Transaction transaction = new Transaction(2L, "ACC789", "ACC123", 250.0, LocalDate.of(2026, 5, 2));

        assertEquals(2L, transaction.getId());
        assertEquals("ACC789", transaction.getSenderAccountNumber());
        assertEquals("ACC123", transaction.getReceiverAccountNumber());
        assertEquals(250.0, transaction.getAmount());
        assertEquals(LocalDate.of(2026, 5, 2), transaction.getTransactionDate());
    }
}