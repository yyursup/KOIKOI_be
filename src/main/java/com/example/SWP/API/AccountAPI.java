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

    @GetMapping("get")
    public ResponseEntity getAllList1(){
        List<ViewProfileResponse> accountList = accountService.getAllAccount();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("viewProfile/{token}")
    public ResponseEntity ViewProfile(String token){
        ViewProfileResponse view = accountService.viewProfile(token);
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

    @PostMapping("/verifyEmail/{email}")
    public  ResponseEntity verifyEmail (@PathVariable String email){
        ForgotPassword forgotPassword = accountService.verifyEmail(email);
        return ResponseEntity.ok(forgotPassword);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public  ResponseEntity verifyOTP (@PathVariable Integer otp, @PathVariable String email){
        ForgotPassword forgotPassword = accountService.verifyOTP(otp,email);
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
        ChangePassword changePassword1 = accountService.changePassword(changePassword,email);
        return ResponseEntity.ok(changePassword1);
    }



}
