package com.example.SWP.model;

import com.example.SWP.entity.Account;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Email
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailBody {
    Account to;

    String subject;

    @Column(nullable = false)
    Date expirationTime;

    String link;

    int otp;
}
