package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
public class ConsigmentService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String ConsignmenType;

    String Description;

    double Price;

    @OneToMany(mappedBy = "consigmentService", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<ConsigmentAgreement> consigmentAgreements;

}
