package com.app.onlineshop.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long customerId;
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private boolean isActive;
    private Date lastOrder;
    private String pic;
}
