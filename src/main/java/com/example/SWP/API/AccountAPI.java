package com.example.SWP.API;

import com.example.SWP.Service.AccountService;
import com.example.SWP.entity.Account;
import com.example.SWP.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
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
        LoginResponse newAccount = accountService.login(loginRequest);
        return ResponseEntity.ok(newAccount);


    }

    @GetMapping("get")
    public ResponseEntity getAllList(){
       List<Account> accountList = accountService.getAllAccount();
         return ResponseEntity.ok(accountList);
    }

    @PutMapping("update/{id}")
    public ResponseEntity updateStudent(@Valid @RequestBody Account account, @PathVariable long id){
        try{
            Account newAccount = accountService.accountUpdate(account,id);
            return ResponseEntity.ok(newAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account " + id + " not found ");
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteAccount(@PathVariable long id){
        try{
            Account oldAccount = accountService.deleteAccount(id);
            return ResponseEntity.ok(oldAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account : " + id + "not found");
        }
    }
}
