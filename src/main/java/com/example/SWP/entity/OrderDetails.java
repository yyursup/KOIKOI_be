package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String Price;

    int quantity;

    @ManyToOne
    @JoinColumn(name = "KoiOrderID")
    KoiOrder koiOrder;

    @ManyToOne
    @JoinColumn(name = "KoiID")
    Koi koi;



}
