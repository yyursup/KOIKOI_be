package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Koi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String image;
    String name;
    String description;
    float price;

    @OneToMany(mappedBy = "koi")
    @JsonIgnore
    List<OrderDetail> orderDetailList;
}
