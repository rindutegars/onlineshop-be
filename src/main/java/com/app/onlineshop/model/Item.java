package com.app.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;
    @Column(nullable = false)
    private String itemName;
    private String itemCode;
    private int stock;
    private Double price;
    private boolean isAvailable;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRestock;
}
