package com.example.SWP.entity;

import com.example.SWP.Enums.TransactionsEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double totalAmount;

    Date transactionsDate;

    @Enumerated(EnumType.STRING)
    TransactionsEnum status;

    String description;

    @ManyToOne
    @JoinColumn(name = "form_id")
    Account from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    Account to;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

    @JsonIgnore
    @OneToOne(mappedBy = "transactions", cascade = CascadeType.ALL)
    SystemProfit systemProfit;
}

