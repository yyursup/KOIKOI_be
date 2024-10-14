package com.example.SWP.model.Request;

import lombok.Data;

@Data
public class OrderCancelRequest {
    String canceledNote;
    String note;
}