package com.app.onlineshop.DTO.request;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer address is required")
    private String customerAddress;

    @NotBlank(message = "Customer code is required")
    private String customerCode;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String customerPhone;

    private boolean isActive;

    // @NotNull(message = "Last order date is required")
    // private Date lastOrder;

    @NotBlank(message = "Picture is required")
    private String pic;
}
