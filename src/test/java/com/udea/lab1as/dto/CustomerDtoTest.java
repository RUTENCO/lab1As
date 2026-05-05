package com.udea.lab1as.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CustomerDtoTest {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void allArgsConstructor_shouldPopulateFields() {
        CustomerDto dto = new CustomerDto(1L, "John", "Doe", "ACC123", 1000.0);

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("ACC123", dto.getAccountNumber());
        assertEquals(1000.0, dto.getBalance());
    }

    @Test
    void serializingNullFields_shouldOmitThemFromJson() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");

        String json = OBJECT_MAPPER.writeValueAsString(dto);

        assertFalse(json.contains("id"));
        assertFalse(json.contains("accountNumber"));
        assertFalse(json.contains("balance"));
        assertEquals(true, json.contains("firstName"));
        assertEquals(true, json.contains("lastName"));
    }

    @Test
    void validateEmptyDto_shouldReturnAllRequiredViolations() {
        CustomerDto dto = new CustomerDto();

        Set<ConstraintViolation<CustomerDto>> violations = VALIDATOR.validate(dto);

        assertEquals(4, violations.size());
    }

    @Test
    void validateValidDto_shouldReturnNoViolations() {
        CustomerDto dto = new CustomerDto(null, "John", "Doe", "ACC123", 0.0);

        Set<ConstraintViolation<CustomerDto>> violations = VALIDATOR.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validateBlankAndNegativeValues_shouldReturnExpectedMessages() {
        CustomerDto dto = new CustomerDto(null, " ", "", "", -1.0);

        Set<ConstraintViolation<CustomerDto>> violations = VALIDATOR.validate(dto);

        assertEquals(4, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")
                && v.getMessage().equals("firstName is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")
                && v.getMessage().equals("lastName is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("accountNumber")
                && v.getMessage().equals("accountNumber is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("balance")
                && v.getMessage().equals("balance must be positive or zero")));
    }

    private static void assertTrue(boolean condition) {
        org.junit.jupiter.api.Assertions.assertTrue(condition);
    }
}