package com.udea.lab1as.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    @NotBlank(message = "senderAccountNumber is required")
    private String senderAccountNumber;
    @NotBlank(message = "receiverAccountNumber is required")
    private String receiverAccountNumber;
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    private Double amount;

    @NotNull(message = "transactionDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
}
