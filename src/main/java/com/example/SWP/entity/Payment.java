package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double PaymentAmount;

    Date PaymentDate;

    String PaymentType;

    @OneToMany(mappedBy = "payment")
    @JsonIgnore
    List<PaymentMethod> paymentMethods;
}
