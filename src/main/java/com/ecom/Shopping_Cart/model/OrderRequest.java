package com.ecom.Shopping_Cart.model;

import lombok.Data;

@Data
public class OrderRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String MobNo;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String paymentType;
}
