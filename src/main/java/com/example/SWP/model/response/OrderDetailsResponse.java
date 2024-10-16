package com.example.SWP.model.response;

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
}
