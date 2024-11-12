package com.example.SWP.model.request;

import lombok.Data;

@Data
public class TransactionRequest {
    double amount;
    String accountNumber;
    String accountName;
    String bankName;
}
