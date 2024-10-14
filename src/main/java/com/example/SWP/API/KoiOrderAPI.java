package com.example.SWP.API;

import com.example.SWP.Service.KoiOrderService;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.model.Request.OrderCancelRequest;
import com.example.SWP.model.Request.OrderCreationRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@SecurityRequirement(name="api")
@CrossOrigin("*")
public class KoiOrderAPI {

    @Autowired
    KoiOrderService koiOrderService;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderCreationRequest orderCreationRequest){
        return ResponseEntity.ok(koiOrderService.convertCartToOrder(orderCreationRequest));
    }

    @GetMapping
    public ResponseEntity getOrderByAccountById(){
        return ResponseEntity.ok(koiOrderService.getKoiOrderByAccountId());
    }

    @GetMapping("all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(koiOrderService.getAllOrders());
    }
    @GetMapping("pending")
    public ResponseEntity getPendingOrders() {
        return ResponseEntity.ok(koiOrderService.getPendingOrders());
    }

    @GetMapping("confirmed")
    public ResponseEntity getConfirmedOrders() {
        return ResponseEntity.ok(koiOrderService.getConfirmedOrders());
    }

    @GetMapping("processing")
    public ResponseEntity getProcessingOrders() {
        return ResponseEntity.ok(koiOrderService.getProcessingOrders());
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity cancelOrder(@PathVariable long id, @RequestBody OrderCancelRequest request) {
        return ResponseEntity.ok(koiOrderService.cancelOrder(id, request));
    }

    @PutMapping("refund/{id}")
    public ResponseEntity refund(@PathVariable long id) {
        return ResponseEntity.ok(koiOrderService.refund(id));
    }

    @GetMapping("{id}")
    public ResponseEntity getOrderById(@PathVariable long id) {
        KoiOrder order = koiOrderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("canceled")
    public ResponseEntity getListCanceledOrder() {
        return ResponseEntity.ok(koiOrderService.getListCancelOrders());
    }


}
