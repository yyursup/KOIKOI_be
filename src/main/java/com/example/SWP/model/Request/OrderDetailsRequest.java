package com.example.SWP.model.Request;

import lombok.Data;

@Data
public class OrderDetailsRequest {

    long koiId;

    int quantity;

    String image;

    String product;

    int price;
}
