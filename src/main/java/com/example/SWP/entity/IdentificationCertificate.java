package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
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

    @OneToOne
    Koi koi;

}
