package com.app.onlineshop.DTO.request;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ItemRequest {
    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Item code is required")
    private String itemCode;

    @Min(value = 0, message = "Stock must be non-negative")
    private int stock;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    private boolean isAvailable;

    private Date lastRestock;
}
