package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double subTotal;

    double shippingPee;

    double totalAmount;

    String status;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<CartDetails> cartDetails;

    @ManyToOne
    @JoinColumn(name = "VoucherID")
    Voucher voucher;

    @OneToOne(mappedBy = "cart",cascade = CascadeType.ALL)
    @JsonIgnore
    Account account;

}
