package com.example.SWP.entity;

import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
public class KoiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double shippingPee;

    String fullName;

    String phone;

    @CreationTimestamp
    LocalDateTime orderDate;

    String note;

    String address;

    double totalAmount;

    Date confirmDate;

    Date processingDate;


    String image;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    Type type;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "koiOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<OrderDetails> orderDetails = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "koiOrder", cascade = CascadeType.ALL)
    CanceledOrder canceledOrder;

    @OneToOne(mappedBy = "koiOrder")
    Payment payment;




}
