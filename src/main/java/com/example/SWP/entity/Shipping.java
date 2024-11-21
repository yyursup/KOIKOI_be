package com.example.SWP.entity;

import com.example.SWP.Enums.StatusShipping;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String codeShipping;

    String name;

    String phone;

    Date deliveryDate;

    Date deliveredDate;

    String address;

    double toTalAmount;

    double shippingPee;

    @Enumerated(EnumType.STRING)
    StatusShipping shipping;

    @ManyToOne
    @JoinColumn(name = "koiOrder_id")
    @JsonIgnore
    KoiOrder koiOrder;
}
