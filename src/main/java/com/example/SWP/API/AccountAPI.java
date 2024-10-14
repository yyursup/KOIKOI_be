package com.example.SWP.API;

import com.example.SWP.Service.AccountService;
import com.example.SWP.model.Request.LoginRequest;
import com.example.SWP.model.Request.RegisterRequest;
import com.example.SWP.model.Request.UpdateProfileRequest;
import com.example.SWP.model.Response.LoginResponse;
import com.example.SWP.model.Response.RegisterResponse;
import com.example.SWP.model.Response.UpdateAndDeleteProfileResponse;
import com.example.SWP.model.Response.ViewProfileResponse;
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



}

