package com.udea.lab1as.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.udea.lab1as.dto.CustomerDto;
import com.udea.lab1as.entity.Customer;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void toDto_shouldMapAllFields() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAccountNumber("ACC123");
        customer.setBalance(1000.0);

        CustomerDto dto = customerMapper.toDto(customer);

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("ACC123", dto.getAccountNumber());
        assertEquals(1000.0, dto.getBalance());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        CustomerDto dto = new CustomerDto(1L, "Jane", "Smith", "ACC456", 2000.0);

        Customer customer = customerMapper.toEntity(dto);

        assertEquals(1L, customer.getId());
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("ACC456", customer.getAccountNumber());
        assertEquals(2000.0, customer.getBalance());
    }

    @Test
    void toDto_whenNull_shouldReturnNull() {
        assertNull(customerMapper.toDto(null));
    }

    @Test
    void toEntity_whenNull_shouldReturnNull() {
        assertNull(customerMapper.toEntity(null));
    }
}