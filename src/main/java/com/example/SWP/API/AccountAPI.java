package com.example.SWP.API;

import com.example.SWP.Service.AccountService;
import com.example.SWP.model.request.LgGg;
import com.example.SWP.model.request.LoginRequest;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.LoginResponse;
import com.example.SWP.model.response.RegisterResponse;
import com.example.SWP.model.response.UpdateAndDeleteProfileResponse;
import com.example.SWP.model.response.ViewProfileResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity getAllList(){
        List<ViewProfileResponse> accountList = accountService.getAllAccount();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("customer")
    public ResponseEntity getAllList2(){
        List<ViewProfileResponse> accountList = accountService.getAllAccount1();
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

    @PostMapping("/loginGoogle")
    public ResponseEntity<?> loginGoogle(@RequestBody LgGg token) {
        return ResponseEntity.ok(accountService.loginGoogle(token));
    }

    // Endpoint để lấy số dư của người dùng hiện tại
    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance() {
        try {
            // Gọi phương thức getBalance từ service để lấy số dư
            double balance = accountService.getBalance();
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu có vấn đề xảy ra
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}

