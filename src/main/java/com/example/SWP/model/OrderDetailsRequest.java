package com.example.SWP.model;

import lombok.Data;

@Data
public class OrderDetailsRequest {
    String image;

    String product;

    int quantity;

    String action;
}
