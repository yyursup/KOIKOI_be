package com.example.SWP.model;

import com.example.SWP.entity.Voucher;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OrderRequest {
    long id;

    double shippingPee;

    LocalDateTime create_At;





}
