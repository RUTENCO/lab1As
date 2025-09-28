package com.udea.lab1As.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data  // genera getters, setters, toString, hashCode, equals
@Entity //definir la clase como una entidad
@Table(name = "customers", schema = "public") //definir el nombre de la tabla
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //generacion automatica del id
    private Long id;

    @Column(unique = true, nullable = false) // no permitir nulos y unico
    private String accountNumber;

    @Column(nullable = false, length = 50) // longitud maxima de 50 caracteres
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private Double balance;

    // constructores
    public Customer() {}

    @JsonCreator // indica que este constructor se usa para crear objetos desde JSON
    public Customer
    (
            @JsonProperty("accountNumber") String accountNumber,  // mapea el JSON a este parametro
            @JsonProperty("balance") Double balance, @JsonProperty("firstName") 
            String firstName, @JsonProperty("id") Long id, @JsonProperty("lastName") 
            String lastName
    ) 
    {

            this.accountNumber = accountNumber;
            this.balance = balance;
            this.firstName = firstName;
            this.id = id;
            this.lastName = lastName;
    }

    // getters y setters se generan con @Data

}
