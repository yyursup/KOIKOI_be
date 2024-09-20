package com.example.SWP.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class Consigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    long AccountID;

    Date Start_Date;

    Date End_Date;

    String Status;

}
