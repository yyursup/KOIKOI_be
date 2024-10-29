package com.example.SWP.model.response;

import com.example.SWP.Enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaymentResponse {
    long id;
    double totalAmount;
    Date paymentDate;
    PaymentType method;
}
