package com.example.SWP.model.request;


import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordRequest {
    @Size(min = 6, message = "Password must be at least 6 characters!")
    String password;

}
