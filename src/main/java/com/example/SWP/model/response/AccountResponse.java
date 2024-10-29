package com.example.SWP.model.response;

import com.example.SWP.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Role Role;
    private String token;
}
