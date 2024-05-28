package com.app.onlineshop.DTO.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemResponse {
    private Long itemId;
    private String itemName;
    private String itemCode;
    private int stock;
    private Double price;
    private boolean isAvailable;
    private Date lastRestock;
}
