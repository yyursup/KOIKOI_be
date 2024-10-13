package com.example.SWP.model;

import com.example.SWP.Enums.Role;
import lombok.Data;

@Data
public class LoginResponse {
    String username;
    String token;
    Role role;
}
