package com.udea.lab1as.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class InsufficientFundsExceptionTest {

    @Test
    void constructor_shouldPreserveMessage() {
        var exception = new InsufficientFundsException("Insufficient funds");

        assertEquals("Insufficient funds", exception.getMessage());
    }
}
