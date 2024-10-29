package com.example.SWP.model.response;

import com.example.SWP.Enums.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KoiOrderResponse {
     Long id;
     double totalAmount;
     double shippingPee;
     String country;
     String city;
     String address;
     String phone;
     Date orderDate;
     String fullName;
     String email;
     OrderStatus status;
}
