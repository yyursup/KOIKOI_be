package com.example.SWP.model;

import lombok.Data;

@Data
public class LoginResponse {
    String username;
    String password;
    String token;
}
