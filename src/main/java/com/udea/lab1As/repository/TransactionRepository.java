package com.udea.lab1As.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udea.lab1As.entity.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // JpaRepository proporciona métodos CRUD y de paginación
    // No es necesario implementar métodos adicionales a menos que se requiera una consulta personalizada
    // Ejemplo de método de consulta personalizada
    // se expone en el servicio para buscar transacciones por cuenta de origen o destino
    List<Transaction> findBySenderAccountNumberOrReceiverAccountNumber(String senderAccountNumber, 
                                                                       String receiverAccountNumber);

}
