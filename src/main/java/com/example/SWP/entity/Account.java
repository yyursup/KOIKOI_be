package com.example.SWP.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    boolean isDeleted = false;

    @NotBlank(message = "This username can not be empty!")
    @Column(unique = true)
    String Username;

    @NotBlank(message = "This full name can not be empty!")
    String Fullname;


    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Phone invalid!")
    @Column(unique = true)
    String Phone_number;

    @Email(message = "Email not valid!")
    @Column(unique = true)
    String email;

    Date create_date;

    String status;

    @NotBlank(message = "Password can not blank!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    int role;

    String city;

    String state;

    String country;

    String specific_Address;


}
