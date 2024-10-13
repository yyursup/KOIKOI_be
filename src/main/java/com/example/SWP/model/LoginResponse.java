package com.example.SWP.model;

import com.example.SWP.entity.Role;
import lombok.Data;

@Data
public class LoginResponse {
    String username;
    String password;
    String token;
    Role role;
}
