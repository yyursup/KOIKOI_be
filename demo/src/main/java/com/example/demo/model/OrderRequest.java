package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    List<OrderDetailRequest> detail;
}
