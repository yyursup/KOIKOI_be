package com.example.SWP.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     long id;

     boolean isDeleted = false;

    @Column(unique = true)
     UUID code;

     String description;

    @NotNull(message = "Discount amount can not null")
     double discount_amount;

     LocalDateTime start_date;

     LocalDateTime end_date;

     String is_active;

    @OneToMany(mappedBy = "voucher")
     List<KoiOrder> koiOrders;


    public void setStartDate(LocalDateTime start_date) {
        this.start_date = start_date;

        this.end_date = this.start_date.plusDays(30);
    }
}
