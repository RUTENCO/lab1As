package com.udea.lab1As.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udea.lab1As.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // JpaRepository proporciona métodos CRUD y de paginación
    // No es necesario implementar métodos adicionales a menos que se requiera una consulta personalizada
    // Optional porque puede no encontrar el cliente, este retorna un objeto Optional que puede contener un cliente o estar vacío
    Optional<Customer> findByAccountNumber(String accountNumber); // buscar cliente por número de cuenta
}
