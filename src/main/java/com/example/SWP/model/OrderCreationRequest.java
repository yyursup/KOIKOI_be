package com.example.SWP.model;

import com.example.SWP.Enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderCreationRequest {

    String phone;

    String fullname;

    LocalDate orderDate;

    String note;

    String address;

    UUID voucher;

    OrderStatus orderStatus = OrderStatus.PENDING;
}
