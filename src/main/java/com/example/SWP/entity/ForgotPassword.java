package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long fpid;

    @Column(nullable = false)
    int otp;

    @Column(nullable = false)
    Date expirationTime;

    @OneToOne
    Account account;
}
