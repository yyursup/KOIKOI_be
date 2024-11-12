package com.example.SWP.model.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class FeedBackRequest {
    String email;
    String feedBackContent;
    Date dateFeedback;


}


