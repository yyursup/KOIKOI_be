package com.example.SWP.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ConsignmentRequest {

    LocalDate start_date;

    LocalDate end_date;
}
