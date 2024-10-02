package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Entity
public class KoiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    String customerId;

    double total_Amount;

    String description;

    String status;

    String comment;

    int rating_stars;

    @NotBlank(message = "Address can not be empty!")
    @Size(min = 6, message = "The address have at least 6 characters")
    String shipping_Address;

    @Column(unique = true)
    String shipping_Code;

    String type_Order;

    Date create_At;


    @Email(message = "Email not valid!")
    @Column(unique = true)
    String email;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;

    @ManyToOne
    @JoinColumn(name = "VoucherID")
    Voucher voucher;

    @OneToMany(mappedBy = "koiOrder" )
    @JsonIgnore
    List<OrderDetails> orderDetails;


}
