package com.example.SWP.model;

import com.example.SWP.entity.Voucher;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OrderRequest {
    long id;

    double subTotal;

    double shippingPee;

    double totalAmount;

    LocalDateTime create_At;





}
