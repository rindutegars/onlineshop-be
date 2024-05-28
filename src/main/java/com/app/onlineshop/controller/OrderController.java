package com.app.onlineshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.app.onlineshop.DTO.request.ItemRequest;
import com.app.onlineshop.DTO.request.OrderRequest;
import com.app.onlineshop.DTO.response.ItemResponse;
import com.app.onlineshop.DTO.response.OrderResponse;
import com.app.onlineshop.model.Item;
import com.app.onlineshop.model.Order;
import com.app.onlineshop.service.OrderService;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Endpoint get all items
    @GetMapping
    public ResponseEntity<Object> getAllOrders() {
        try {
            List<Order> items = orderService.getAllOrders();
            if (items.isEmpty()) {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("message", "No orders found.");
                responseBody.put("status", HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            String errorMessage = "Error fetching orders: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to get item by Id
    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<Order> orderOptional = orderService.getOrderById(orderId);
            if (orderOptional.isPresent()) {
                return ResponseEntity.ok(orderOptional.get());
            } else {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                String errorMessage = "Order with ID " + orderId + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
        } catch (Exception e) {
            String errorMessage = "Error fetching item: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to create order
    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderRequest requestDTO) {
        try {
            OrderResponse response = orderService.createOrder(requestDTO);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("order", response);
            responseBody.put("message", "Order created successfully.");
            responseBody.put("status", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "";
            if (e.getMessage().contains("Customer not found")) {
                errorMessage = "Customer with ID " + requestDTO.getCustomerId() + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else if (e.getMessage().contains("Item not found")) {
                errorMessage = "Item with ID " + requestDTO.getItemId() + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else {
                errorMessage = "Error creating order: " + e.getMessage();
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }
        }
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<Object> updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequest requestDTO) {
        try {
            OrderResponse response = orderService.updateOrder(orderId, requestDTO);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("order", response);
            responseBody.put("message", "Order with ID " + orderId + " successfully updated.");
            responseBody.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "Order with ID " + orderId + " not found.";
            responseBody.put("message", errorMessage);
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "";
            if (e.getMessage().contains("Customer not found")) {
                errorMessage = "Customer with ID " + requestDTO.getCustomerId() + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else if (e.getMessage().contains("Item not found")) {
                errorMessage = "Item with ID " + requestDTO.getItemId() + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else {
                errorMessage = "Error updating order: " + e.getMessage();
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }
        }
    }

    // Endpoint to delete an order by ID
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String message = "Order with ID " + orderId + " successfully deleted.";
            responseBody.put("message", message);
            responseBody.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "Order with ID " + orderId + " not found.";
            responseBody.put("message", errorMessage);
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error deleting order: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Global exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}