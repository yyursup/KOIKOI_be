package com.example.SWP.API;

import com.example.SWP.Service.TransactionsService;
import com.example.SWP.entity.Transactions;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transactions")
@SecurityRequirement(name="api")
@CrossOrigin("*")
public class TransactionsAPI {

    @Autowired
    TransactionsService transactionsService;

    @PostMapping("transactions")
    public ResponseEntity create(@RequestParam Long koiOrderId) throws Exception {

         transactionsService.createTransactions(koiOrderId);
        return ResponseEntity.ok("SUCCESS");
    }
}
