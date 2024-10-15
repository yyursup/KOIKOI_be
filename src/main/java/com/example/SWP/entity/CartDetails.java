package com.example.SWP.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String image;

    String description;

    String name;

    double  price;

    int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CartID")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "KoiID")
    Koi koi;
}
