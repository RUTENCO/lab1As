package com.udea.lab1as.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udea.lab1as.dto.CustomerDto;
import com.udea.lab1as.entity.Customer;
import com.udea.lab1as.exception.CustomerNotFoundException;
import com.udea.lab1as.mapper.CustomerMapper;
import com.udea.lab1as.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAccountNumber("ACC123");
        customer.setBalance(1000.0);

        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setAccountNumber("ACC123");
        customerDto.setBalance(1000.0);
    }

    @Test
    void getAllCustomers_shouldMapAllEntitiesToDtos() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(2L);
        anotherCustomer.setFirstName("Jane");
        anotherCustomer.setLastName("Smith");
        anotherCustomer.setAccountNumber("ACC456");
        anotherCustomer.setBalance(2000.0);

        CustomerDto anotherDto = new CustomerDto();
        anotherDto.setId(2L);
        anotherDto.setFirstName("Jane");
        anotherDto.setLastName("Smith");
        anotherDto.setAccountNumber("ACC456");
        anotherDto.setBalance(2000.0);

        when(customerRepository.findAll()).thenReturn(List.of(customer, anotherCustomer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);
        when(customerMapper.toDto(anotherCustomer)).thenReturn(anotherDto);

        var result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void getCustomerById_whenExists_shouldReturnMappedDto() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        var result = customerService.getCustomerById(1L);

        assertEquals("ACC123", result.getAccountNumber());
    }

    @Test
    void getCustomerById_whenMissing_shouldThrowNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        var exception = assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(99L));

        assertEquals("Customer not found with id: 99", exception.getMessage());
    }

    @Test
    void createCustomer_shouldSaveAndReturnMappedDto() {
        CustomerDto input = new CustomerDto(null, "Alice", "Brown", "ACC789", 500.0);
        Customer mappedCustomer = new Customer();
        mappedCustomer.setFirstName("Alice");
        mappedCustomer.setLastName("Brown");
        mappedCustomer.setAccountNumber("ACC789");
        mappedCustomer.setBalance(500.0);

        Customer savedCustomer = new Customer();
        savedCustomer.setId(3L);
        savedCustomer.setFirstName("Alice");
        savedCustomer.setLastName("Brown");
        savedCustomer.setAccountNumber("ACC789");
        savedCustomer.setBalance(500.0);

        CustomerDto savedDto = new CustomerDto(3L, "Alice", "Brown", "ACC789", 500.0);

        when(customerMapper.toEntity(input)).thenReturn(mappedCustomer);
        when(customerRepository.save(mappedCustomer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(savedDto);

        var result = customerService.createCustomer(input);

        assertEquals(3L, result.getId());
        assertEquals("Alice", result.getFirstName());
        verify(customerRepository).save(mappedCustomer);
    }

    @Test
    void updateCustomer_shouldApplyOnlyNonNullFields() {
        CustomerDto patch = new CustomerDto();
        patch.setFirstName("John Updated");
        patch.setBalance(1500.0);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setFirstName("John Updated");
        updatedCustomer.setLastName("Doe");
        updatedCustomer.setAccountNumber("ACC123");
        updatedCustomer.setBalance(1500.0);

        CustomerDto updatedDto = new CustomerDto(1L, "John Updated", "Doe", "ACC123", 1500.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        when(customerMapper.toDto(updatedCustomer)).thenReturn(updatedDto);

        var result = customerService.updateCustomer(1L, patch);

        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("ACC123", result.getAccountNumber());
        assertEquals(1500.0, result.getBalance());
    }

    @Test
    void updateCustomer_whenMissing_shouldThrowNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        var exception = assertThrows(CustomerNotFoundException.class,
                () -> customerService.updateCustomer(99L, customerDto));

        assertEquals("Customer not found with id: 99", exception.getMessage());
    }

    @Test
    void deleteCustomer_whenExists_shouldDeleteById() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteCustomer(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_whenMissing_shouldThrowNotFound() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        var exception = assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(99L));

        assertEquals("Customer not found with id: 99", exception.getMessage());
    }

    @Test
    void getCustomerByAccountNumber_whenExists_shouldReturnMappedDto() {
        when(customerRepository.findByAccountNumber("ACC123")).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        var result = customerService.getCustomerByAccountNumber("ACC123");

        assertEquals("John", result.getFirstName());
        assertEquals("ACC123", result.getAccountNumber());
    }

    @Test
    void getCustomerByAccountNumber_whenMissing_shouldThrowNotFound() {
        when(customerRepository.findByAccountNumber("UNKNOWN")).thenReturn(Optional.empty());

        var exception = assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerByAccountNumber("UNKNOWN"));

        assertEquals("Customer not found with account number: UNKNOWN", exception.getMessage());
    }
}
