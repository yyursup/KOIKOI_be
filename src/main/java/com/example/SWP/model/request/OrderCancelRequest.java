package com.example.SWP.model.request;

import lombok.Data;

@Data
public class OrderCancelRequest {
    String canceledNote;
    String note;
}