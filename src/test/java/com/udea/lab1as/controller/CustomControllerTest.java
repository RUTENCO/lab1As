package com.udea.lab1as.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.lab1as.dto.CustomerDto;
import com.udea.lab1as.exception.CustomerNotFoundException;
import com.udea.lab1as.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private CustomerDto customerDto;
    private List<CustomerDto> customerList;

    @BeforeEach
    void setUp() {
        // Inyectar el mock de Mockito en el controlador
        ReflectionTestUtils.setField(customerController, "customerService", customerService);

        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setAccountNumber("ACC123");
        customerDto.setBalance(1000.00);  // Double, not BigDecimal

        CustomerDto customer2 = new CustomerDto();
        customer2.setId(2L);
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setAccountNumber("ACC456");
        customer2.setBalance(2000.00);

        customerList = Arrays.asList(customerDto, customer2);
    }

    // --- GET /api/customers ---
    @Test
    void getAllCustomers_shouldReturnOkWithList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customerList);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    // --- GET /api/customers/{id} ---
    @Test
    void getCustomerById_whenExists_shouldReturnOk() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customerDto);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getCustomerById_whenNotFound_shouldReturn404() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    // --- POST /api/customers (with validation) ---
    @Test
    void createCustomer_withValidData_shouldReturn201() throws Exception {
        CustomerDto input = new CustomerDto();
        input.setFirstName("Alice");
        input.setLastName("Brown");
        input.setAccountNumber("ACC789");
        input.setBalance(500.00);

        CustomerDto created = new CustomerDto();
        created.setId(3L);
        created.setFirstName("Alice");
        created.setLastName("Brown");
        created.setAccountNumber("ACC789");
        created.setBalance(500.00);

        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    void createCustomer_withMissingFirstName_shouldReturn400() throws Exception {
        CustomerDto invalid = new CustomerDto();
        invalid.setLastName("Brown");
        invalid.setAccountNumber("ACC789");
        invalid.setBalance(500.00);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomer_withMissingLastName_shouldReturn400() throws Exception {
        CustomerDto invalid = new CustomerDto();
        invalid.setFirstName("Alice");
        invalid.setAccountNumber("ACC789");
        invalid.setBalance(500.00);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomer_withMissingAccountNumber_shouldReturn400() throws Exception {
        CustomerDto invalid = new CustomerDto();
        invalid.setFirstName("Alice");
        invalid.setLastName("Brown");
        invalid.setBalance(500.00);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomer_withMissingBalance_shouldReturn400() throws Exception {
        CustomerDto invalid = new CustomerDto();
        invalid.setFirstName("Alice");
        invalid.setLastName("Brown");
        invalid.setAccountNumber("ACC789");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    // --- PUT /api/customers/{id} ---
    @Test
    void updateCustomer_whenExists_shouldReturnOk() throws Exception {
        CustomerDto updated = new CustomerDto();
        updated.setId(1L);
        updated.setFirstName("John Updated");
        updated.setLastName("Doe");
        updated.setAccountNumber("ACC123");
        updated.setBalance(1500.00);

        when(customerService.updateCustomer(eq(1L), any(CustomerDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John Updated"));
    }

    @Test
    void updateCustomer_whenNotFound_shouldReturn404() throws Exception {
        when(customerService.updateCustomer(eq(99L), any(CustomerDto.class)))
            .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(put("/api/customers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /api/customers/{id} ---
    @Test
    void deleteCustomer_whenExists_shouldReturn204() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_whenNotFound_shouldReturn404() throws Exception {
        doThrow(new CustomerNotFoundException("Customer not found")).when(customerService).deleteCustomer(99L);

        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    // --- GET /api/customers/account/{accountNumber} ---
    @Test
    void getCustomerByAccountNumber_whenExists_shouldReturnOk() throws Exception {
        when(customerService.getCustomerByAccountNumber("ACC123")).thenReturn(customerDto);

        mockMvc.perform(get("/api/customers/account/ACC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC123"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getCustomerByAccountNumber_whenNotFound_shouldReturn404() throws Exception {
        when(customerService.getCustomerByAccountNumber("UNKNOWN"))
            .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/account/UNKNOWN"))
                .andExpect(status().isNotFound());
    }
}