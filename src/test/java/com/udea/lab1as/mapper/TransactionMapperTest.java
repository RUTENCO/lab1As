package com.udea.lab1as.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.udea.lab1as.dto.TransactionDto;
import com.udea.lab1as.entity.Transaction;

class TransactionMapperTest {

    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    void toDto_shouldMapAllFields() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSenderAccountNumber("ACC123");
        transaction.setReceiverAccountNumber("ACC456");
        transaction.setAmount(100.0);
        transaction.setTransactionDate(LocalDate.of(2026, 5, 3));

        TransactionDto dto = transactionMapper.toDto(transaction);

        assertEquals(1L, dto.getId());
        assertEquals("ACC123", dto.getSenderAccountNumber());
        assertEquals("ACC456", dto.getReceiverAccountNumber());
        assertEquals(100.0, dto.getAmount());
        assertEquals(LocalDate.of(2026, 5, 3), dto.getTransactionDate());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        TransactionDto dto = new TransactionDto(1L, "ACC789", "ACC123", 250.0, LocalDate.of(2026, 5, 2));

        Transaction transaction = transactionMapper.toEntity(dto);

        assertEquals(1L, transaction.getId());
        assertEquals("ACC789", transaction.getSenderAccountNumber());
        assertEquals("ACC123", transaction.getReceiverAccountNumber());
        assertEquals(250.0, transaction.getAmount());
        assertEquals(LocalDate.of(2026, 5, 2), transaction.getTransactionDate());
    }

    @Test
    void toDto_whenNull_shouldReturnNull() {
        assertNull(transactionMapper.toDto(null));
    }

    @Test
    void toEntity_whenNull_shouldReturnNull() {
        assertNull(transactionMapper.toEntity(null));
    }
}