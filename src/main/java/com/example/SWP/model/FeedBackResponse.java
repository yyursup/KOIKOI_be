package com.example.SWP.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FeedBackResponse {
    long  id;

    LocalDate feedBackDay;

    String feedBackContent;
}
