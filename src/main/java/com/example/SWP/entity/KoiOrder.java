package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class KoiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double subTotal;

    double shippingPee;

    String fullname;

    String phone;

    @CreationTimestamp
    LocalDateTime orderDate;

    String note;

    String address;

    double totalAmount;

    Date confirmDate;

    Date processingDate;

    Date shippingDate;

    Date deliveryDate;

    String image;

    String status;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;

    @OneToMany(mappedBy = "koiOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    List<OrderDetails> orderDetails;

    @OneToOne
    Payment payment;




}
