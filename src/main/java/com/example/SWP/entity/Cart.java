package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double subTotal;

    double shippingPee;

    double totalAmount;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<CartDetails> cartDetails = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    Voucher voucher;

    @OneToOne(mappedBy = "cart",cascade = CascadeType.ALL)
    @JsonIgnore
    Account account;

}
