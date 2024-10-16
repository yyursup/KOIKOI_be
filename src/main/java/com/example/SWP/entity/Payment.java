package com.example.SWP.entity;

import com.example.SWP.Enums.PaymentType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double PaymentAmount;

    Date paymentDate;

    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<PaymentMethod> paymentMethods;

    @OneToOne
    @JoinColumn(name = "koiOrder_id")
    KoiOrder koiOrder;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Transactions> transactions;
}
