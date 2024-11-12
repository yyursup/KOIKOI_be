package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    long id;

    Date feedBackDay;

    String feedBackContent;

    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Account account;
}
