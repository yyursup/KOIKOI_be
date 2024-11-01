package com.example.demo.model;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetailRequest {
    UUID koiId;
    int quantity;
}
