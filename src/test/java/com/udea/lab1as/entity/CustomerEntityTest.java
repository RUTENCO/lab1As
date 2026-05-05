package com.udea.lab1as.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CustomerEntityTest {

    @Test
    void defaultConstructorAndSetters_shouldPopulateFields() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setAccountNumber("ACC123");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setBalance(1000.0);

        assertEquals(1L, customer.getId());
        assertEquals("ACC123", customer.getAccountNumber());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals(1000.0, customer.getBalance());
        assertNotNull(customer.toString());
    }

    @Test
    void jsonCreatorConstructor_shouldPopulateFields() {
        Customer customer = new Customer("ACC456", 2000.0, "Jane", 2L, "Smith");

        assertEquals(2L, customer.getId());
        assertEquals("ACC456", customer.getAccountNumber());
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals(2000.0, customer.getBalance());
    }
}