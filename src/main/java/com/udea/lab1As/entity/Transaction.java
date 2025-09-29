package com.udea.lab1As.entity;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data // genera getters, setters, toString, hashCode, equals
@Entity //definir la clase como una entidad

@Table(name="transactions", schema="public") //definir el nombre de la tabla
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generacion automatica del id
    private Long id;

    @Column(name = "sender_account_number", nullable = false) //definir el nombre de la columna
    private String senderAccountNumber;

    @Column(name = "receiver_account_number", nullable = false)// no permitir nulos
    private String receiverAccountNumber;

    @Column(nullable = false) // sino se define el nombre toma el de la variable
    private Double amount;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    // constructores
    public Transaction() {}

    @JsonCreator // indica que este constructor se usa para crear objetos desde JSON
    public Transaction
    (
            @JsonProperty("id") Long id, // mapea el JSON a este parametro
            @JsonProperty("senderAccountNumber") String senderAccountNumber, 
            @JsonProperty("receiverAccountNumber") String receiverAccountNumber, 
            @JsonProperty("amount") Double amount,
            @JsonProperty("transactionDate") LocalDate transactionDate
    ) 
    {
            this.id = id;
            this.senderAccountNumber = senderAccountNumber;
            this.receiverAccountNumber = receiverAccountNumber;
            this.amount = amount;
            this.transactionDate = transactionDate;
    }

    // getters y setters se generan con @Data

    

}
