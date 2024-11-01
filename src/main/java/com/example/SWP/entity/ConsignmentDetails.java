package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConsignmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    double price;

    int quantity;

    String image;

    @ManyToOne
    @JoinColumn(name = "consignment_id")
    @JsonIgnore
    Consignment consignment;


    @ManyToOne
    @JoinColumn(name = "order_details_id")
    @JsonIgnore
    OrderDetails orderDetails;

    @OneToOne
    @JoinColumn(name = "koi_id")
    @JsonIgnore
    Koi koi;
}