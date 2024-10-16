package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "PaymentID")
    Payment payment;


}
