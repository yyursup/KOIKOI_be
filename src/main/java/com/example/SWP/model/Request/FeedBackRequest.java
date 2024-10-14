package com.example.SWP.model.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FeedBackRequest {

    LocalDate feedBackDay;

    String feedBackContent;

}
