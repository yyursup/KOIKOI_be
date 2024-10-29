package com.example.SWP.API;

import com.example.SWP.Service.ManagerService;
import com.example.SWP.entity.Transactions;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Manager")
@CrossOrigin("*")
@SecurityRequirement(name = "api")

public class ManagerAPI {

    @Autowired
    ManagerService managerService;



    @PostMapping("registerForManager")
    public ResponseEntity registerManager(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse newAccount = managerService.registerForManager(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("Profile")
    public ResponseEntity ViewProfile(){
        ViewProfileResponse view = managerService.viewProfile();
        return ResponseEntity.ok(view);
    }


    @PutMapping("{id}")
    public ResponseEntity updateAccount(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, @PathVariable Long id){
        try{
            UpdateAndDeleteProfileResponse newAccount = managerService.accountUpdate(updateProfileRequest,id);
            return ResponseEntity.ok(newAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account " + id + " not found ");
        }

    }
//
//    @GetMapping
//    public ResponseEntity transactionsIncome(@RequestParam int month, @RequestParam int year) {
//            List<TransactionsResponse> profit = managerService.transactionsIncome(month, year);
//            return ResponseEntity.ok(profit);
//    }

    @GetMapping("payment")
    public ResponseEntity transactionsIncome2(@RequestParam int month, @RequestParam int year) {
        List<PaymentResponse> profit = managerService.paymentsIncome(month, year);
        return ResponseEntity.ok(profit);
    }

    @GetMapping("/totalProfit")
    public ResponseEntity getTotalSystemProfit() {
        Double totalProfit = managerService.getTotalIncome();
        return ResponseEntity.ok(totalProfit);
    }


}
