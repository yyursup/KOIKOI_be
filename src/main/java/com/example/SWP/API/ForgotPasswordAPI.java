package com.example.SWP.API;

import com.example.SWP.Service.ForgotPasswordService;
//import com.example.SWP.entity.ForgotPassword;
//import com.example.SWP.model.ChangePassword;
import com.example.SWP.model.request.ForgotPasswordRequest;
import com.example.SWP.model.request.ResetPasswordRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ForgotPasswordAPI {

    @Autowired
    ForgotPasswordService forgotPasswordService;


    @PostMapping("forgot-password")
    public ResponseEntity forgotPassword( @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest)  {
        forgotPasswordService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Forgot Password successfully");
    }

    @PostMapping("reset-password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        forgotPasswordService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Reset Password successfully");
    }
}
