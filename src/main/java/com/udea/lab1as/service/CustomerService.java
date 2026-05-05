package com.udea.lab1as.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udea.lab1as.dto.CustomerDto;
import com.udea.lab1as.exception.CustomerNotFoundException;
import com.udea.lab1as.mapper.CustomerMapper;
import com.udea.lab1as.repository.CustomerRepository;

@Service
public class CustomerService {
    // son final porque se inyectan a través del constructor y no deben cambiarse
    private final CustomerRepository customerRepository; // Repositorio para acceder a los datos de clientes
    private final CustomerMapper customerMapper; // Mapper para convertir entre entidad y DTO

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    // Aquí se pueden agregar métodos para manejar la lógica de negocio relacionada
    // con los clientes
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
        return customerMapper.toDto(requireCustomerById(id));
    }

    // crear un nuevo cliente
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        // var, es una forma de declarar variables locales sin especificar el tipo
        // explícitamente
        // el tipo se infiere automáticamente a partir del valor asignado
        var customer = customerMapper.toEntity(customerDto); // convierte el DTO a una entidad
        var savedCustomer = customerRepository.save(customer); // guarda la entidad en la base de datos
        return customerMapper.toDto(savedCustomer); // convierte la entidad guardada de vuelta a un DTO y lo retorna
    }

    // actualizar un cliente existente
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        var existingCustomer = requireCustomerById(id);
        applyCustomerUpdates(existingCustomer, customerDto);
        var updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }

    // eliminar un cliente por su id
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // buscar cliente por número de cuenta
    public CustomerDto getCustomerByAccountNumber(String accountNumber) {
        return customerMapper.toDto(requireCustomerByAccount(accountNumber));
    }

    private com.udea.lab1as.entity.Customer requireCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    private com.udea.lab1as.entity.Customer requireCustomerByAccount(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with account number: " + accountNumber));
    }

    private void applyCustomerUpdates(com.udea.lab1as.entity.Customer existingCustomer, CustomerDto customerDto) {
        if (customerDto.getFirstName() != null) {
            existingCustomer.setFirstName(customerDto.getFirstName());
        }
        if (customerDto.getLastName() != null) {
            existingCustomer.setLastName(customerDto.getLastName());
        }
        if (customerDto.getAccountNumber() != null) {
            existingCustomer.setAccountNumber(customerDto.getAccountNumber());
        }
        if (customerDto.getBalance() != null) {
            existingCustomer.setBalance(customerDto.getBalance());
        }
    }
}