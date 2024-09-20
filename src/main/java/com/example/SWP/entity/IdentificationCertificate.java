package com.example.SWP.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class IdentificationCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String Name;

    String Breeder;

    String Variety;

    Date Date_Of_Birth;

    Date Date_Of_Import;

    Float Size;

    String Link_Certificate;

}
