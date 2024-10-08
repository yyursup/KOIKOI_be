package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String description;

    String image;

    String name;

    double  price;

    int quantity;

    @ManyToOne
    @JoinColumn(name = "CartID")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "KoiID")
    Koi koi;
}
