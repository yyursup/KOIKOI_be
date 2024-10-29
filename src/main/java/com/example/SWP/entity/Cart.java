package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    //@JsonIgnore
    Voucher voucher;

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    Account account;


    public List<CartDetails> getActiveCartDetails() {
        return cartDetails.stream()
                .filter(cartDetail -> !cartDetail.isDeleted())
                .collect(Collectors.toList());
    }

}
