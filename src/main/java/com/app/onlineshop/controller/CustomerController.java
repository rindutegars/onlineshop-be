package com.app.onlineshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.app.onlineshop.DTO.request.CustomerRequest;
import com.app.onlineshop.DTO.response.CustomerResponse;
import com.app.onlineshop.model.Customer;
import com.app.onlineshop.service.CustomerService;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Endpoint to get all customers
    @GetMapping
    public ResponseEntity<Object> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            if (customers.isEmpty()) {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("message", "No customers found.");
                responseBody.put("status", HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            String errorMessage = "Error fetching customers: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to get customer by Id
    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long customerId) {
        try {
            Optional<Customer> customerOptional = customerService.getCustomerById(customerId);
            if (customerOptional.isPresent()) {
                return ResponseEntity.ok(customerOptional.get());
            } else {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                String errorMessage = "Customer with ID " + customerId + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
        } catch (Exception e) {
            String errorMessage = "Error fetching customer: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to create customer
    @PostMapping("/create")
    public ResponseEntity<Object> createCustomerWithPic(@ModelAttribute @Valid CustomerRequest requestDTO) {
        try {
            CustomerResponse response = customerService.createCustomer(requestDTO);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("customer", response);
            responseBody.put("message", "Customer created successfully.");
            responseBody.put("status", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error creating customer: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to update a customer by ID
    @PutMapping("/update/{customerId}")
    public ResponseEntity<Object> updateCustomer(
            @PathVariable Long customerId,
            @ModelAttribute @Valid CustomerRequest requestDTO) {
        try {
            CustomerResponse response = customerService.updateCustomer(customerId, requestDTO);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("customer", response);
            responseBody.put("message", "Customer with ID " + customerId + " successfully updated.");
            responseBody.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "Customer with ID " + customerId + " not found.";
            responseBody.put("message", errorMessage);
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error updating customer: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to delete a customer by ID
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String message = "Customer with id " + customerId + " successfully deleted.";
            responseBody.put("message", message);
            responseBody.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "Customer with ID " + customerId + " not found.";
            responseBody.put("message", errorMessage);
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error deleting customer: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("errors", errors);
        responseBody.put("message", "Validation failed");
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
