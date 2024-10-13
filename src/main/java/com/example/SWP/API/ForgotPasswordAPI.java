package com.example.SWP.API;

import com.example.SWP.Service.ForgotPasswordService;
import com.example.SWP.entity.ForgotPassword;
import com.example.SWP.model.ChangePassword;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fp")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ForgotPasswordAPI {

    @Autowired
    ForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity verifyEmail (@PathVariable String email){
        ForgotPassword forgotPassword = forgotPasswordService.verifyEmail(email);
        return ResponseEntity.ok(forgotPassword);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public  ResponseEntity verifyOTP (@PathVariable Integer otp, @PathVariable String email){
        ForgotPassword forgotPassword = forgotPasswordService.verifyOTP(otp,email);
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
        ChangePassword changePassword1 = forgotPasswordService.changePassword(changePassword,email);
        return ResponseEntity.ok(changePassword1);
    }
}
