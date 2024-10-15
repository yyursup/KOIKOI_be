package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    double price;

    int quantity;

    @ManyToOne
    @JoinColumn(name = "KoiOrderID")
    KoiOrder koiOrder;

    @ManyToOne
    @JoinColumn(name = "KoiID")
    Koi koi;

}
