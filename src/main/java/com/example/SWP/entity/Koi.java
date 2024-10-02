package com.example.SWP.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

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

    String img;

    @NotBlank(message = "This size can not be empty!")
    String size;

    @NotBlank(message = "This gender can not be empty!")
    String gender;

    @NotBlank(message = "This status can not be empty!")
    String status;

    @NotBlank(message = "This origin can not be empty!")
    String origin;


    @ManyToOne
    @JoinColumn(name = "ConsigmentID")
    Consigment consigment;

    @ManyToOne
    @JoinColumn(name = "KoiTypeID")
    KoiType koiType;
}
