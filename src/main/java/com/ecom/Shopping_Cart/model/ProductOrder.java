package com.ecom.Shopping_Cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;

    private LocalDate orderDate;

    @ManyToOne
    private Product product;
    private Double price;
    private Integer quantity;

    @ManyToOne
    private userDetails user;
    private String status;
    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderAddress orderAddress;
}
