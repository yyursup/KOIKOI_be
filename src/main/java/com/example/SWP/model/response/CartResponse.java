package com.example.SWP.model.response;

import com.example.SWP.entity.CartDetails;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data

public class CartResponse {
    long id;

    double subTotal;

    double shippingPee;

    double disCount;

    double totalAmount;

    LocalDateTime create_At;



}
