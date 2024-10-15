package com.example.SWP.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class CartResponse {
    long id;

    double subTotal;

    double shippingPee;

    double disCount;

    double totalAmount;

    LocalDateTime create_At;

}
