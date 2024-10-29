package com.example.SWP.model.request;

import com.example.SWP.Enums.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
@Data
public class OrderPayRequest {
    String phone;

    String fullName;

    LocalDate orderDate;

    String note;

    String city;

    String country;

    String gmail;

    String address;

}