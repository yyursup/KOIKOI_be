package com.example.SWP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Koi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "This name can not be empty!")
    String name;

    String description;

    @NotBlank(message = "This price can not be empty!")
    @Pattern(regexp = "\\d{1,3}(.\\d{3})*(\\.\\d+)?\\s?VND", message = "Price Invalid")
    double price;

    @NotBlank(message = "This age can not be empty!")
    int age;

    @NotBlank(message = "This size can not be empty!")
    String size;

    @NotBlank(message = "This gender can not be empty!")
    String gender;

    @NotBlank(message = "This status can not be empty!")
    String status;

    @NotBlank(message = "This origin can not be empty!")
    String origin;


    String KoitypeID;

    String ConsigmentID;

}
