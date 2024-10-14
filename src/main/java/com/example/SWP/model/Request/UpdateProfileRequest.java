package com.example.SWP.model.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank(message = "This username can not be empty!")
    @Column(unique = true)
    String username;

    @NotBlank(message = "This full name can not be empty!")
    String Fullname;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Phone invalid!")
    @Column(unique = true)
    String Phone_number;

    @Email(message = "Email not valid!")
    @Column(unique = true)
    String email;

    String city;

    String state;

    String country;

    String specific_Address;
}
