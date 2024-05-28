package com.app.onlineshop.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String orderCode;
    private Date orderDate;
    private Double totalPrice;
    private int quantity;
    private Long customerId;
    private Long itemId;
}
