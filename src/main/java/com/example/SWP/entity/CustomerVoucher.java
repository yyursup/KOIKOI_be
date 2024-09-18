package com.example.SWP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class CustomerVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date Assigned_date;

    Date used_date;

    int Quantity;


}
