package com.example.SWP.model.response;

import com.example.SWP.Enums.Author;
import com.example.SWP.entity.IdentificationCertificate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KoiResponse {
    long id;

    @NotBlank(message = "This name can not be empty!")
    String name;

    String description;

    double price;

    int age;

    @NotBlank(message = "This size can not be empty!")
    String size;

    @NotBlank(message = "This status can not be empty!")
    String status;

    @NotBlank(message = "This origin can not be empty!")
    String origin;

    int quantity;

    String gender;

    String image;

    String category;

    Author author;

    IdentificationCertificate identificationCertificate;

}
