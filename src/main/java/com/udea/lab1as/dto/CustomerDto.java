package com.udea.lab1as.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;
    @NotBlank(message = "firstName is required")
    private String firstName;
    @NotBlank(message = "lastName is required")
    private String lastName;
    @NotBlank(message = "accountNumber is required")
    private String accountNumber;
    @NotNull(message = "balance is required")
    @PositiveOrZero(message = "balance must be positive or zero")
    private Double balance;

}
