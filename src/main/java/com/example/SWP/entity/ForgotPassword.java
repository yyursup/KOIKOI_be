package com.example.SWP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data


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


    public ForgotPassword(int otp, Date expirationTime, Account account) {
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.account = account;
    }
}
