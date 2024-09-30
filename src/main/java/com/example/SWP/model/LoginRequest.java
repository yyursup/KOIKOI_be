package com.example.SWP.model;

import com.example.SWP.entity.Role;
import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
