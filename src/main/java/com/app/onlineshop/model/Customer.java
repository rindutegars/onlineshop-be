package com.app.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;
    @Column(nullable = false)
    private String customerName;
    private String customerAddress;
    private String customerCode;
    private String customerPhone;
    private boolean isActive;
    private Date lastOrder;
    private String pic;
}
