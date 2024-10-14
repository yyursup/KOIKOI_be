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
    @JoinColumn(name = "Consignment_ID")
    Consignment consignment;

    @ManyToOne
    @JoinColumn(name = "ConsigmentService_ID")
    ConsigmentService consigmentService;


}
