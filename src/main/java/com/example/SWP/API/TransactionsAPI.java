package com.example.SWP.API;

import com.example.SWP.Service.TransactionsService;
import com.example.SWP.entity.Transactions;
import com.example.SWP.model.request.TransactionRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("transactions3")
    public ResponseEntity create3(@RequestParam Long koiOrderId) throws Exception {

        transactionsService.createTransactions3(koiOrderId);
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


    // Nạp tiền cho tài khoản của Manager
    @PostMapping("/top-up")
    public ResponseEntity<String> topUpManagerBalance(@RequestBody TransactionRequest transactionRequest) {
        try {
            transactionsService.topUpManagerBalance(transactionRequest);
            return ResponseEntity.ok("Top-up processed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFunds(@RequestBody TransactionRequest transactionRequest) {
        try {
            transactionsService.withdrawFunds(transactionRequest);
            return ResponseEntity.ok("Withdrawal request processed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    // Endpoint xác nhận yêu cầu rút tiền của user
    @PostMapping("/approveWithdrawal")
    public ResponseEntity<String> approveWithdrawalRequest(@RequestParam long transactionId) {
        try {
            transactionsService.approveWithdrawalRequest(transactionId);
            return ResponseEntity.ok("Withdrawal request approved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint từ chối yêu cầu rút tiền
    @PostMapping("/rejectWithdrawal")
    public ResponseEntity<String> rejectWithdrawalRequest(@RequestParam long transactionId) {
        try {
            transactionsService.rejectWithdrawalRequest(transactionId);
            return ResponseEntity.ok("Withdrawal request rejected successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint lấy danh sách yêu cầu rút tiền chờ xử lý
    @GetMapping("/Withdrawals")
    public ResponseEntity<List<Transactions>> getTransactionsWithoutPayment() {
        List<Transactions> transactions = transactionsService.getTransactionsWithoutPayment();
        return ResponseEntity.ok(transactions);
    }



}
