package com.example.SWP.model;

import lombok.Data;

@Data
public class OrderCancelRequest {
    String canceledNote;
    String note;
}