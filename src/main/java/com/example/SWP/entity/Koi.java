package com.example.SWP.entity;

import com.example.SWP.Enums.Author;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(columnDefinition = "TEXT")
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

    @Column(columnDefinition = "TEXT")
    String image;

    String category;

    Author author;

    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    @JsonIgnore
    List<OrderDetails> orderDetails;

    @ManyToOne
    @JoinColumn(name = "KoiTypeID")
    @JsonIgnore
    KoiType koiType;

    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    @JsonIgnore
    List<CartDetails> cartDetails;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_id")
    Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identification_certificate_id")
    @JsonIgnore
    IdentificationCertificate identificationCertificate;
}
