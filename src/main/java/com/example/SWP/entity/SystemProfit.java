package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SystemProfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    double balance;

    String description;

    Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    Transactions transactions;
}
