package com.example.SWP.API;

import com.example.SWP.Service.TransactionsService;
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

    @PostMapping("transactionsCosign")
    public ResponseEntity createConsign(@RequestParam Long koiOrderId) throws Exception {

        transactionsService.createTransactionsForConsign(koiOrderId);
        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("transactionsCosignForSell")
    public ResponseEntity createConsignForSell(@RequestParam Long koiOrderId) throws Exception {

        transactionsService.BuyKoiFromUser(koiOrderId);
        return ResponseEntity.ok("SUCCESS");
    }
    @PostMapping("refund")
    public ResponseEntity refund(@RequestParam Long koiOrderId) throws Exception {
        transactionsService.createTransactions2(koiOrderId);
        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("extendConsign")
    public ResponseEntity extendConsign(@RequestParam Long koiOrderId) throws Exception {

        transactionsService.createTransactionForExtendConsign(koiOrderId);
        return ResponseEntity.ok("SUCCESS");
    }
    @PostMapping("cancelConsign")
    public ResponseEntity cancelConsign(@RequestParam Long koiOrderId) throws Exception {
        transactionsService.createTransactionForCancelConsign(koiOrderId);
        return ResponseEntity.ok("SUCCESS");
    }

}
