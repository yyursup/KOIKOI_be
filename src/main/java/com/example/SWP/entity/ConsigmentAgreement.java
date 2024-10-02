package com.example.SWP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ConsigmentAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    long Consigmentid;

    String Product_Name;

    int Quantity;

    Float Price;
}
