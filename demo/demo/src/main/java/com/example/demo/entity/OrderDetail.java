package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    int quantity;

    float price;

    @ManyToOne
    @JoinColumn(name = "order_id")
            @JsonIgnore
    Orders orders;

    @ManyToOne
    @JoinColumn(name = "koi_id")
    Koi koi;
}
