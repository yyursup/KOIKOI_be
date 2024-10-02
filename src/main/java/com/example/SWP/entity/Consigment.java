package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Consigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date Start_Date;

    Date End_Date;

    String Status;
}
