package com.udea.lab1As.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class CustomerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String accountNumber;
    private Double balance;

    // constructores
    public CustomerDto() {}

    public  CustomerDto
    (
            Long id,
            String firstName, 
            String lastName,
            String accountNumber,  
            Double balance
    ) 
    {

            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.accountNumber = accountNumber;
            this.balance = balance;
            
    }

    // getters y setters 
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
