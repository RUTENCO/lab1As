package com.udea.lab1as.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.udea.lab1as.dto.CustomerDto;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleCustomerNotFound_shouldReturn404() {
        var response = handler.handleCustomerNotFound(new CustomerNotFoundException("Customer not found"));
        var body = Objects.requireNonNull(response.getBody());

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Customer not found", body.get("message"));
    }

    @Test
    void handleBadRequest_withInvalidTransaction_shouldReturn400() {
        var response = handler.handleBadRequest(new InvalidTransactionException("Transaction amount must be greater than zero"));
        var body = Objects.requireNonNull(response.getBody());

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Transaction amount must be greater than zero", body.get("message"));
    }

    @Test
    void handleBadRequest_withIllegalArgument_shouldReturn400() {
        var response = handler.handleBadRequest(new IllegalArgumentException("Bad argument"));
        var body = Objects.requireNonNull(response.getBody());

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Bad argument", body.get("message"));
    }

    @Test
    void handleBadRequest_withValidationError_shouldUseFirstFieldMessage() throws Exception {
        Method method = DummyController.class.getMethod("create", CustomerDto.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new CustomerDto(), "customerDto");
        bindingResult.addError(new FieldError("customerDto", "firstName", "firstName is required"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        var response = handler.handleBadRequest(exception);
        var body = Objects.requireNonNull(response.getBody());

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("firstName is required", body.get("message"));
    }

    @Test
    void handleInsufficientFunds_shouldReturn422() {
        var response = handler.handleInsufficientFunds(new InsufficientFundsException("Insufficient funds"));
        var body = Objects.requireNonNull(response.getBody());

        assertEquals(422, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Insufficient funds", body.get("message"));
    }

    @Test
    void handleUnexpected_shouldReturn500() {
        var response = handler.handleUnexpected(new RuntimeException("boom"));
        var body = Objects.requireNonNull(response.getBody());

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(body);
        assertEquals("Internal server error", body.get("message"));
    }

    private static class DummyController {
        @SuppressWarnings("unused")
        public void create(CustomerDto customerDto) {
            // Intentionally empty - used only to extract MethodParameter for testing MethodArgumentNotValidException
        }
    }
}