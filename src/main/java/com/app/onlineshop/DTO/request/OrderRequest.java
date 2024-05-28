package com.app.onlineshop.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class OrderRequest {
    @NotBlank(message = "Order code is required")
    private String orderCode;

    @NotNull(message = "Order date is required")
    private Date orderDate;

    @NotNull(message = "Total price is required")
    @Min(value = 0, message = "Total price must be non-negative")
    private Double totalPrice;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Item ID is required")
    private Long itemId;
}
