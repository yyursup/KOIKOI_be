package com.example.SWP.API;

import com.example.SWP.Service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
@SecurityRequirement(name="api")
@CrossOrigin("*")
public class PaymentAPI {

    @Autowired
    PaymentService paymentService;

    @PostMapping("{id}")
    public ResponseEntity create(@RequestParam Long koiOrderId) throws Exception {

        String vnPayURL = paymentService.createUrl(koiOrderId);
        return ResponseEntity.ok(vnPayURL);
    }
}
