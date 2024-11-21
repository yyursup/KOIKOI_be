package com.example.SWP.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class ShippingRequest {
    String shippingCode;
    Date deliveryDate;
    Date deliveredDate;

}
