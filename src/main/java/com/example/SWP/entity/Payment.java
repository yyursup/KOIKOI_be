package com.example.SWP.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String OrderID;
    String ConsigmentID;

    double PaymentAmount;

    Date PaymentDate;

    String PaymentType;
}
