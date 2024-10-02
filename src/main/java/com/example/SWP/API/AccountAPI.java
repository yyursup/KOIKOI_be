package com.example.SWP.API;

import com.example.SWP.Repository.ForgotPasswordRepository;
import com.example.SWP.Service.AccountService;
import com.example.SWP.entity.ForgotPassword;
import com.example.SWP.model.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AccountAPI {
    @Autowired
    AccountService accountService;



    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;



    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse newAccount = accountService.register(registerRequest);
            return ResponseEntity.ok(newAccount);


    }

    @PostMapping("registerForManager")
    public ResponseEntity registerManager(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse newAccount = accountService.registerForManager(registerRequest);
        return ResponseEntity.ok(newAccount);


    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse newAccount = accountService.loginWithUserName(loginRequest);
        return ResponseEntity.ok(newAccount);
    }

//    @GetMapping("get")
//    public ResponseEntity getAllList(){
//       List<Account> accountList = accountService.getAllAccount();
//         return ResponseEntity.ok(accountList);
//    }

    @GetMapping()
    public ResponseEntity getAllList1(){
        List<ViewProfileResponse> accountList = accountService.getAllAccount();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("Profile")
    public ResponseEntity ViewProfile(){
        ViewProfileResponse view = accountService.viewProfile();
        return ResponseEntity.ok(view);
    }



    @PutMapping("{id}")
    public ResponseEntity updateAccount(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, @PathVariable Long id){
        try{
         UpdateAndDeleteProfileResponse newAccount = accountService.accountUpdate(updateProfileRequest,id);
         return ResponseEntity.ok(newAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account " + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteAccount(@PathVariable Long id){
        try{
            UpdateAndDeleteProfileResponse oldAccount = accountService.deleteAccount(id);
            return ResponseEntity.ok(oldAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account : " + id + " not found");
        }
    }

@PostMapping("/ForgotPassword")
public ResponseEntity processForgotPassword(@RequestParam String email, @RequestParam(required = false) Integer otp, @RequestBody(required = false) ChangePassword changePassword) {


    ForgotPassword forgotPassword = accountService.verifyOTPAndChagePassword(email, otp, changePassword);

    if (otp == null) {
        return ResponseEntity.ok("OTP has been sent to your email.");
    }
    if (changePassword != null) {
        return ResponseEntity.ok("OTP verified and password changed successfully");
    }

    return ResponseEntity.badRequest().body("Invalid request. Please provide OTP or email.");
}

}

