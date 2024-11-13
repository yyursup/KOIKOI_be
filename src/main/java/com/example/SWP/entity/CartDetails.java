package com.example.SWP.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(columnDefinition = "TEXT")
    String image;

    @Column(columnDefinition = "TEXT")
    String description;

    String name;

    double  price;

    int quantity;

    boolean isDeleted = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CartID")
    @JsonIgnore
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "KoiID")
    @JsonIgnore
    Koi koi;
}
