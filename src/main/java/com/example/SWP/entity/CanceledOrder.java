package com.example.SWP.entity;

import com.example.SWP.Enums.CancelOrderStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
public class CanceledOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    @JoinColumn(name = "KoiOrder_id")
    KoiOrder koiOrder;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    Date cancelDate;

    String reason;

    double totalAmount;

    @Enumerated(value = EnumType.STRING)
    CancelOrderStatus cancelOrderStatus;
}
