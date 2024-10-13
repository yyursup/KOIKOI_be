package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Koi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "This name can not be empty!")
    String name;

    String description;

    boolean isDeleted = false;

    double price;

    int age;


    @NotBlank(message = "This size can not be empty!")
    String size;

    @NotBlank(message = "This gender can not be empty!")
    String gender;

    @NotBlank(message = "This status can not be empty!")
    String status;

    @NotBlank(message = "This origin can not be empty!")
    String origin;

    int quantity;

    String image;

    String category;


    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    @JsonIgnore
    List<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "ConsigmentID")
    Consigment consigment;

    @ManyToOne
    @JoinColumn(name = "KoiTypeID")
    KoiType koiType;

    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    @JsonIgnore
    List<CartDetails> cartDetails;
}
