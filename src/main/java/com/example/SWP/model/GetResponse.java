package com.example.SWP.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetResponse {
    long id;

    String username;

    String Fullname;

    String Phone_number;

    String email;

    Date create_date;

    String city;

    String state;

    String country;

    String specific_Address;
}
