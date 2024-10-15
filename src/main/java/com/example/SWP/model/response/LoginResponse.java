package com.example.SWP.model.response;

import com.example.SWP.Enums.Role;
import lombok.Data;

@Data
public class LoginResponse {
    String username;
    String token;
    Role role;
}
