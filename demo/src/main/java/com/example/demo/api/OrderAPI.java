package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.entity.Orders;
import com.example.demo.model.OrderRequest;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity create (@RequestBody OrderRequest orderRequest) {
        Orders orders = orderService.create(orderRequest);
        return  ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity get() {
        Account account = authenticationService.getCurrentAccount();
        List<Orders> orders = orderRepository.findOrderssByCustomer(account);
        return ResponseEntity.ok(orders);
    }

}
