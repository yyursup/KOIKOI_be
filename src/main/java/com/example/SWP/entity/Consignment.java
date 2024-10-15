package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Consignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date Start_Date;

    Date End_Date;

    String Status;

    @OneToMany(mappedBy = "consignment", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Koi> kois;

    @OneToOne
    Payment payment;

    @OneToOne(mappedBy = "consignment",cascade = CascadeType.ALL)
    Account account;

}
