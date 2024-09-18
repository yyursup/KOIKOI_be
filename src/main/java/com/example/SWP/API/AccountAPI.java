package com.example.SWP.API;

import com.example.SWP.Service.AccountService;
import com.example.SWP.entity.Account;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AccountAPI {
    @Autowired
    AccountService accountService;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody Account account){
         Account newAccount = accountService.register(account);
         return ResponseEntity.ok(newAccount);
    }

    @GetMapping("get")
    public ResponseEntity getAllList(){
       List<Account> accountList = accountService.getAllAccount();
         return ResponseEntity.ok(accountList);
    }
}
