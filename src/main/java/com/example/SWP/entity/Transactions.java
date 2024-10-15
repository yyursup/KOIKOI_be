package com.example.SWP.entity;

import com.example.SWP.Enums.TransactionsEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter

public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    TransactionsEnum status;

    String description;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

    @ManyToOne
    @JoinColumn(name = "form_id")
    Account from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    Account to;

}
