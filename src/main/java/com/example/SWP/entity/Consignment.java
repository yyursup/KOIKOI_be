package com.example.SWP.entity;

import com.example.SWP.Enums.StatusConsign;
import com.example.SWP.Enums.TypeOfConsign;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Consignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    LocalDate start_date;

    LocalDate end_date;

    String product_name;

    String note;

    double totalAmount;

    @Enumerated(EnumType.STRING)
    StatusConsign status;


    @Enumerated(EnumType.STRING)
    TypeOfConsign type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "consignment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<ConsignmentDetails> consignmentDetailsSet = new HashSet<>();


}
