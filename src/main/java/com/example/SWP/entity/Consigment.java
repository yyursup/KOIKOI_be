package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Entity
@Data
public class Consigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date Start_Date;

    Date End_Date;

    String Status;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;



}
