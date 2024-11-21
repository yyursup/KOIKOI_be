package com.example.SWP.model.request;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateRequest {
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
