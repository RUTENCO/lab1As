package com.udea.lab1as.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TransactionDtoTest {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    @Test
    void allArgsConstructor_shouldPopulateFields() {
        LocalDate date = LocalDate.of(2026, 5, 3);
        TransactionDto dto = new TransactionDto(1L, "ACC123", "ACC456", 250.0, date);

        assertEquals(1L, dto.getId());
        assertEquals("ACC123", dto.getSenderAccountNumber());
        assertEquals("ACC456", dto.getReceiverAccountNumber());
        assertEquals(250.0, dto.getAmount());
        assertEquals(date, dto.getTransactionDate());
    }

    @Test
    void serializingNullFields_shouldOmitThemFromJson() throws Exception {
        TransactionDto dto = new TransactionDto();
        dto.setSenderAccountNumber("ACC123");
        dto.setTransactionDate(LocalDate.of(2026, 5, 3));

        String json = OBJECT_MAPPER.writeValueAsString(dto);

        assertFalse(json.contains("id"));
        assertFalse(json.contains("receiverAccountNumber"));
        assertFalse(json.contains("amount"));
        assertTrue(json.contains("senderAccountNumber"));
        assertTrue(json.contains("2026-05-03"));
    }

    @Test
    void validateEmptyDto_shouldReturnAllRequiredViolations() {
        TransactionDto dto = new TransactionDto();

        Set<ConstraintViolation<TransactionDto>> violations = VALIDATOR.validate(dto);

        assertEquals(4, violations.size());
    }

    @Test
    void validateValidDto_shouldReturnNoViolations() {
        TransactionDto dto = new TransactionDto(null, "ACC123", "ACC456", 10.0, LocalDate.of(2026, 5, 3));

        Set<ConstraintViolation<TransactionDto>> violations = VALIDATOR.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validateInvalidValues_shouldReturnExpectedMessages() {
        TransactionDto dto = new TransactionDto(null, " ", "", -1.0, null);

        Set<ConstraintViolation<TransactionDto>> violations = VALIDATOR.validate(dto);

        assertEquals(4, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("senderAccountNumber")
                && v.getMessage().equals("senderAccountNumber is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("receiverAccountNumber")
                && v.getMessage().equals("receiverAccountNumber is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")
                && v.getMessage().equals("amount must be greater than zero")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("transactionDate")
                && v.getMessage().equals("transactionDate is required")));
    }
}