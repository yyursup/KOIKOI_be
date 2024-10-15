package com.example.SWP.model.request;

import lombok.Data;

@Data
public class OrderDetailsRequest {

    long koiId;

    int quantity;

    String image;

    String product;

    int price;
}
