package com.example.SWP.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Voucher {
    @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      long id;



    @NotBlank(message = "code can not blank")
    String code;

    String description;

    int discount_amount;

    Date start_date;

    Date end_date;

    String is_active;


}
