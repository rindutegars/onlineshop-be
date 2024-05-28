package com.app.onlineshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.app.onlineshop.DTO.request.CustomerRequest;
import com.app.onlineshop.DTO.request.ItemRequest;
import com.app.onlineshop.DTO.response.CustomerResponse;
import com.app.onlineshop.DTO.response.ItemResponse;
import com.app.onlineshop.model.Customer;
import com.app.onlineshop.model.Item;
import com.app.onlineshop.service.ItemService;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Endpoint get all items
    @GetMapping
    public ResponseEntity<Object> getAllItems() {
        try {
            List<Item> items = itemService.getAllItems();
            if (items.isEmpty()) {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                responseBody.put("message", "No items found.");
                responseBody.put("status", HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            String errorMessage = "Error fetching items: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to get item by Id
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId) {
        try {
            Optional<Item> itemOptional = itemService.getItemById(itemId);
            if (itemOptional.isPresent()) {
                return ResponseEntity.ok(itemOptional.get());
            } else {
                Map<String, Object> responseBody = new LinkedHashMap<>();
                String errorMessage = "Item with ID " + itemId + " not found.";
                responseBody.put("message", errorMessage);
                responseBody.put("status", HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
        } catch (Exception e) {
            String errorMessage = "Error fetching item: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to create item
    @PostMapping("/create")
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemRequest requestDTO) {
        try {
            ItemResponse response = itemService.createItem(requestDTO);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("item", response);
            responseBody.put("message", "Item created successfully.");
            responseBody.put("status", HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error creating item: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to update an item by ID
    @PutMapping("/update/{itemId}")
    public ResponseEntity<Object> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemRequest requestDTO) {
        try {
            ItemResponse response = itemService.updateItem(itemId, requestDTO);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            responseBody.put("item", response);
            responseBody.put("message", "Item with ID " + itemId + " successfully updated.");
            responseBody.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "Item with ID " + itemId + " not found.";
            responseBody.put("message", errorMessage);
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error updating item: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    // Endpoint to delete an item by ID
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long itemId) {
        try {
            itemService.deleteItem(itemId);
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String message = "Item with ID " + itemId + " successfully deleted.";
            responseBody.put("message", message);
            responseBody.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, Object> responseBody = new LinkedHashMap<>();
            String errorMessage = "Item with ID " + itemId + " not found.";
            responseBody.put("message", errorMessage);
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            String errorMessage = "Error deleting item: " + e.getMessage();
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