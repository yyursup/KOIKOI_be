package com.example.SWP.model;

import com.example.SWP.entity.Account;
import jakarta.validation.constraints.Email;
import lombok.*;


@Getter
@Setter
@Email
@AllArgsConstructor
@NoArgsConstructor
public class MailBody {
    Account to;
    String subject;
    String link;
}
