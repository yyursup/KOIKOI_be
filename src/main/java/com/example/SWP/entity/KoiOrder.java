package com.example.SWP.entity;

import com.example.SWP.Enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class KoiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

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

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;

    @OneToMany(mappedBy = "koiOrder", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<OrderDetails> orderDetails;

    @JsonIgnore
    @OneToOne(mappedBy = "koiOrder", cascade = CascadeType.ALL)
    CanceledOrder canceledOrder;

    @OneToOne
    Payment payment;




}
