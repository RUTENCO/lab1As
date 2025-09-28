package com.udea.lab1As.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udea.lab1As.dto.CustomerDto;
import com.udea.lab1As.mapper.CustomerMapper;  
import com.udea.lab1As.repository.CustomerRepository;  

@Service
public class CustomerService {
    // son final porque se inyectan a través del constructor y no deben cambiarse
    private final CustomerRepository customerRepository; // Repositorio para acceder a los datos de clientes
    private final CustomerMapper customerMapper; // Mapper para convertir entre entidad y DTO

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    // Aquí se pueden agregar métodos para manejar la lógica de negocio relacionada con los clientes
    // Por ejemplo, crear, actualizar, eliminar y buscar clientes

    // obtener la informacion de todos los clientes
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll() // obtiene todos los clientes de la base de datos
                .stream() // convierte la lista a un stream, que permite operaciones funcionales
                .map(customerMapper::toDto) // mapea cada entidad Customer a un CustomerDto usando el mapper
                .toList(); // convierte el stream de vuelta a una lista
    }

    // obtener la informacion de un cliente por su id
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findById(id) // busca un cliente por su id
                .map(customerMapper::toDto) // si se encuentra, mapea la entidad a un DTO
                .orElseThrow(() -> new RuntimeException("Customer not found")); // si no se encuentra, lanza una excepción
    }

    // crear un nuevo cliente
    public CustomerDto createCustomer(CustomerDto customerDto) {
        // var, es una forma de declarar variables locales sin especificar el tipo explícitamente
        // el tipo se infiere automáticamente a partir del valor asignado
        var customer = customerMapper.toEntity(customerDto); // convierte el DTO a una entidad
        var savedCustomer = customerRepository.save(customer); // guarda la entidad en la base de datos
        return customerMapper.toDto(savedCustomer); // convierte la entidad guardada de vuelta a un DTO y lo retorna
    }
}