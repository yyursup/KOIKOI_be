package com.example.SWP.model.response;

import lombok.Data;

import java.util.Date;
@Data
public class ViewProfileResponse {
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
