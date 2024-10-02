package com.example.SWP.entity;

import jakarta.persistence.*;

@Entity
public class ConsigmentAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    String Product_Name;

    int Quantity;

    Float Price;


}
