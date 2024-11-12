package com.example.SWP.model.response;

import lombok.Data;

import java.util.Date;
@Data
public class TransactionsResponse {
     long id;
     double totalAmount;
     Date transactionsDate;
     String description;
     String accountNumber;
     String accountName;
     String bankName;
}

