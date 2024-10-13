package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    long id;

    LocalDate feedBackDay;

    String feedBackContent;

    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    KoiOrder koiOrder;


}
