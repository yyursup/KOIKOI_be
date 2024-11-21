package com.example.SWP.model.response;

import com.example.SWP.Enums.StatusOrderDetails;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String image;
    private StatusOrderDetails status;
}
