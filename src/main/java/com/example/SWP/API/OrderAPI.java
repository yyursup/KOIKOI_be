package com.example.SWP.API;

import com.example.SWP.Service.OrderService;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.model.OrderRequest;
import com.example.SWP.model.VoucherRequest;
import com.example.SWP.model.VoucherRequestForDiscount;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    OrderService orderService;

    @PostMapping("{id}")
    public ResponseEntity newOrder(@Valid @RequestBody OrderRequest orderRequest ) {
        OrderRequest newOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(newOrder);
    }

    @PostMapping("{id}/applyVoucher")
    public ResponseEntity applyVoucher(@PathVariable Long id, @RequestBody VoucherRequestForDiscount voucherRequestForDiscount){
        OrderRequest updateOrder = orderService.applyVoucher(id,voucherRequestForDiscount.getCode());
        return ResponseEntity.ok(updateOrder);
    }
}
