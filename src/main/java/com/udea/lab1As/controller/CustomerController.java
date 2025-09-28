package com.udea.lab1As.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udea.lab1As.dto.CustomerDto;
import com.udea.lab1As.service.CustomerService;


@RestController // Indica que esta clase es un controlador REST

@RequestMapping( value = "/api/customers", produces = "application/json") // Ruta base para todas las solicitudes en este controlador
public class CustomerController {

    // ejeemplo de inyección de dependencias sin usar @Autowired
    // se inyecta a través del constructor
    private final CustomerService customerService; // final porque se inyecta a través del constructor y no debe cambiarse

    public CustomerController(CustomerService customerService) { // inyección de dependencias a través del constructor
        this.customerService = customerService;
    }

    // recursos HTTP ---> URL
    // métodos HTTP ---> GET, POST, PUT, DELETE
    // representación del recurso ---> JSON, XML; texto plano
    // código de estado HTTP ---> 200 OK, 201 Created, 400 Bad Request, 404 Not Found, 500 Internal Server Error

    // obtener todos los clientes
    // GET /api/customers ---> obtener todos los clientes
    @GetMapping // Maneja solicitudes GET a /api/customers
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // obtener un cliente por su id
    // GET /api/customers/{id} ---> obtener un cliente por su id
    @GetMapping("/{id}") // Maneja solicitudes GET a /api/customers/{id}
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // crear un nuevo cliente
    // POST /api/customers ---> crear un nuevo cliente
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        if (customerDto.getFirstName() == null || customerDto.getLastName() == null ||
            customerDto.getAccountNumber() == null || customerDto.getBalance() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        // el código de estado 201 Created es más apropiado para indicar que un recurso ha sido creado exitosamente
        // además, se puede incluir la ubicación del nuevo recurso en los encabezados de la respuesta
        // body contiene el cliente creado
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    // actualizar un cliente existente
    // PUT /api/customers/{id} ---> actualizar un cliente por su id
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        try {
            CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // eliminar un cliente
    // DELETE /api/customers/{id} ---> eliminar un cliente por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // obtener cliente por número de cuenta
    // GET /api/customers/account/{accountNumber} ---> obtener cliente por número de cuenta
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<CustomerDto> getCustomerByAccountNumber(@PathVariable String accountNumber) {
        try {
            CustomerDto customer = customerService.getCustomerByAccountNumber(accountNumber);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
