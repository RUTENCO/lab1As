package com.udea.lab1As.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class TransactionDto {
    private Long id;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private Double amount;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    // constructores
    public TransactionDto() {}

    public TransactionDto
    (
            Long id, 
            String senderAccountNumber, 
            String receiverAccountNumber, 
            Double amount,
            LocalDate transactionDate
    ) 
    {
            this.id = id;
            this.senderAccountNumber = senderAccountNumber;
            this.receiverAccountNumber = receiverAccountNumber;
            this.amount = amount;
            this.transactionDate = transactionDate;
    }

    // getters y setters 
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }
    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }
    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
