package com.udea.lab1As.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

}
