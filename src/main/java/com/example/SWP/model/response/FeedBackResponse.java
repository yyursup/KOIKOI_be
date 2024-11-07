package com.example.SWP.model.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class FeedBackResponse {
    long  id;

    Date feedBackDay;

    String feedBackContent;

    String email;
}
