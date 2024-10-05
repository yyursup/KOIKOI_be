package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class KoiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double subTotal;

    double shippingPee;

    double totalAmount;

    boolean isDeleted = false;

    String status;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;

    @ManyToOne
    @JoinColumn(name = "VoucherID")
    Voucher voucher;

    @OneToMany(mappedBy = "koiOrder")
    @JsonIgnore
    List<OrderDetails> orderDetails;

    @OneToOne
    Payment payment;




}
