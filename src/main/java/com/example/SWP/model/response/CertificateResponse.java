package com.example.SWP.model.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateResponse {
    long id;

    String Name;

    String Breeder;

    String Variety;

    Date Date_Of_Birth;

    Date Date_Of_Import;

    Float Size;

    @Column(columnDefinition = "TEXT")
    String image;
}
