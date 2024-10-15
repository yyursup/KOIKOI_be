package com.example.SWP.model.request;

import com.example.SWP.Enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderCreationRequest {

    String phone;

    String fullName;

    LocalDate orderDate;

    String note;

    String address;

    UUID voucher;

    OrderStatus orderStatus = OrderStatus.PENDING;

}
