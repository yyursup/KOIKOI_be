package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ConsigmentAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    String Product_Name;

    int Quantity;

    double Price;

    @ManyToOne
    @JoinColumn(name = "Consigment_ID")
    Consigment consigment;

    @ManyToOne
    @JoinColumn(name = "ConsigmentService_ID")
    ConsigmentService consigmentService;


}
