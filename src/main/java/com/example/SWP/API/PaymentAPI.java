package com.example.SWP.API;

import com.example.SWP.Service.EmailService;
import com.example.SWP.Service.PaymentService;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.model.request.OrderCreationRequest;
import com.example.SWP.model.request.OrderPayRequest;
import com.example.SWP.model.response.KoiOrderResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("api/payment")
@SecurityRequirement(name="api")
@CrossOrigin("*")
public class PaymentAPI {

    @Autowired
    PaymentService paymentService;

    @Autowired
    EmailService emailService;

    // Endpoint tạo URL thanh toán VNPay
    @PostMapping("/VNPay")
    public ResponseEntity create(@RequestParam Long koiOrderId, @RequestBody OrderPayRequest request) throws Exception {
        String vnPayURL = paymentService.createUrl(koiOrderId, request);
        return ResponseEntity.ok(vnPayURL);
    }

    // Endpoint lấy đơn hàng thanh toán gần nhất
    @GetMapping("/lastPaidOrder")
    public ResponseEntity getLastPaidOrder() {
        KoiOrderResponse response = paymentService.getLastPaidOrder();
        return ResponseEntity.ok(response);
    }
}
