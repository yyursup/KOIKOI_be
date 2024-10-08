package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Consigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date Start_Date;

    Date End_Date;

    String Status;

    @ManyToOne
    @JoinColumn(name = "AccountID")
    Account account;

    @OneToMany(mappedBy = "consigment", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Koi> kois;

    @OneToOne
    Payment payment;

    @OneToMany(mappedBy = "consigment", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<ConsigmentAgreement> consigmentAgreements;

}
