package com.example.SWP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Koi {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            long id;

    String name;

    String description;

    double price;

    int age;

    String size;

    String gender;

    String status;

    String origin;

    String KoitypeID;

    String ConsigmentID;

}
