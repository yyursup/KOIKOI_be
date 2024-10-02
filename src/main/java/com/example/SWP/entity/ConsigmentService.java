package com.example.SWP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ConsigmentService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String Type_Name;

    String Description;

    double Price;
}
