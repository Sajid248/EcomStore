package com.ecom.Shopping_Cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private userDetails user;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double totalOrderPrice;


}
