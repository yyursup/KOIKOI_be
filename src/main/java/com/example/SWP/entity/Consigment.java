package com.example.SWP.entity;

import jakarta.persistence.*;

import java.util.Date;

public class Consigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    long AccountID;

    Date Start_Date;

    Date End_Date;

    String Status;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;

}
