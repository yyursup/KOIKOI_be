package com.example.SWP.entity;

import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.Type;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class KoiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double shippingPee;

    String fullName;

    String phone;

    Date orderDate;

    String city;

    String country;

    String note;

    String address;

    String email;

    double totalAmount;

    Date confirmDate;

    Date processingDate;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    Type type;

    String vnPayTxRef;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "koiOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    Set<OrderDetails> orderDetails = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "koiOrder", cascade = CascadeType.ALL)
    CanceledOrder canceledOrder;

    @OneToOne(mappedBy = "koiOrder")
    @JsonIgnore
    Payment payment;

    @OneToMany(mappedBy = "koiOrder",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    List<Shipping> shippingList;

    @ManyToOne
    @JoinColumn(name = "consignment_id")
    @JsonIgnore
    Consignment consignment;
}

