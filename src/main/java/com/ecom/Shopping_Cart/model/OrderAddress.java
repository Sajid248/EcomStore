package com.ecom.Shopping_Cart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;
import lombok.Data;

@Data
@Entity
public class OrderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String MobNo;
    private String email;
    private String address;
    private String city;
    private String state;
    private String pincode;
}
