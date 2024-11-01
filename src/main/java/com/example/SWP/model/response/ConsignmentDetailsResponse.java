package com.example.SWP.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsignmentDetailsResponse {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String image;
}
