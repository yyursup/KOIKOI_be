package com.example.SWP.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CartRequest {

    long id;

    double shippingPee;

    LocalDateTime create_At;
}
