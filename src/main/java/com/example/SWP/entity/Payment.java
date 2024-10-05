package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double PaymentAmount;

    @NotBlank(message = "Address can not be empty!")
    @Size(min = 6, message = "The address have at least 6 characters")
    String shipping_Address;

    @Column(unique = true)
    String shipping_Code;

    Date PaymentDate;


    String PaymentType;

    @Email(message = "Email not valid!")
    @Column(unique = true)
    String email;

    @OneToMany(mappedBy = "payment")
    @JsonIgnore
    List<PaymentMethod> paymentMethods;
}
