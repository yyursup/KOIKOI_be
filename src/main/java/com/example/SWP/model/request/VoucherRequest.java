package com.example.SWP.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VoucherRequest {

    long id;

    @NotNull
    UUID code;

    String description;

    @NotNull(message = "Discount amount can not be null")
    double discount_amount;



    LocalDateTime start_date;

    LocalDateTime end_date;

    String is_active;

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date.plusDays(30);
    }
}