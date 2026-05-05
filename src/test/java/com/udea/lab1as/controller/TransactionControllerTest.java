package com.udea.lab1as.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.lab1as.dto.TransactionDto;
import com.udea.lab1as.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private final TransactionDto transactionDto = createTransactionDto(1L, "ACC123", "ACC456", 250.0,
            LocalDate.of(2026, 5, 3));
    private final List<TransactionDto> transactionList = Arrays.asList(
            transactionDto,
            createTransactionDto(2L, "ACC789", "ACC123", 150.0, LocalDate.of(2026, 5, 2)));

    private static TransactionDto createTransactionDto(Long id, String senderAccountNumber,
            String receiverAccountNumber, Double amount, LocalDate transactionDate) {
        TransactionDto dto = new TransactionDto();
        dto.setId(id);
        dto.setSenderAccountNumber(senderAccountNumber);
        dto.setReceiverAccountNumber(receiverAccountNumber);
        dto.setAmount(amount);
        dto.setTransactionDate(transactionDate);
        return dto;
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionController, "transactionService", transactionService);
    }

    @Test
    void transferMoney_withValidData_shouldReturn201() throws Exception {
        TransactionDto input = new TransactionDto(null, "ACC123", "ACC456", 250.0, LocalDate.of(2026, 5, 3));
        TransactionDto created = new TransactionDto(10L, "ACC123", "ACC456", 250.0, LocalDate.of(2026, 5, 3));

        when(transactionService.transferMoney(any(TransactionDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.senderAccountNumber").value("ACC123"));
    }

    @Test
    void transferMoney_withMissingSender_shouldReturn400() throws Exception {
        TransactionDto invalid = new TransactionDto(null, null, "ACC456", 250.0, LocalDate.of(2026, 5, 3));

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferMoney_withMissingAmount_shouldReturn400() throws Exception {
        TransactionDto invalid = new TransactionDto(null, "ACC123", "ACC456", null, LocalDate.of(2026, 5, 3));

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferMoney_withMissingDate_shouldReturn400() throws Exception {
        TransactionDto invalid = new TransactionDto(null, "ACC123", "ACC456", 250.0, null);

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTransactionsByAccountNumber_shouldReturnOkWithList() throws Exception {
        when(transactionService.getTransactionsByAccountNumber("ACC123")).thenReturn(transactionList);

        mockMvc.perform(get("/api/transactions/ACC123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].senderAccountNumber").value("ACC789"));
    }

    @Test
    void getTransactionsByAccountNumber_withNoTransactions_shouldReturnEmptyList() throws Exception {
        when(transactionService.getTransactionsByAccountNumber("EMPTY")).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/EMPTY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}