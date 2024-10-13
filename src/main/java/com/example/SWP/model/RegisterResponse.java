package com.example.SWP.model;

import com.example.SWP.entity.Role;
import lombok.Data;

@Data
public class RegisterResponse {
    long id;
    String username;
    String Fullname;
    String Phone_number;
    String email;
}
