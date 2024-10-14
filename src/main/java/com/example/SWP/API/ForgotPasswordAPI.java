package com.example.SWP.API;

import com.example.SWP.Service.AccountService;
import com.example.SWP.Service.ForgotPasswordService;
//import com.example.SWP.entity.ForgotPassword;
//import com.example.SWP.model.ChangePassword;
import com.example.SWP.model.ForgotPasswordRequest;
import com.example.SWP.model.ResetPasswordRequest;
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

//    @PostMapping("/verifyEmail/{email}")
//    public ResponseEntity verifyEmail (@PathVariable String email){
//        ForgotPassword forgotPassword = forgotPasswordService.verifyEmail(email);
//        return ResponseEntity.ok(forgotPassword);
//    }
//
//    @PostMapping("/verifyOtp/{otp}/{email}")
//    public  ResponseEntity verifyOTP (@PathVariable Integer otp, @PathVariable String email){
//        ForgotPassword forgotPassword = forgotPasswordService.verifyOTP(otp,email);
//        return ResponseEntity.ok("OTP verified");
//    }
//
//    @PostMapping("/changePassword/{email}")
//    public ResponseEntity changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
//        ChangePassword changePassword1 = forgotPasswordService.changePassword(changePassword,email);
//        return ResponseEntity.ok(changePassword1);
//    }

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
