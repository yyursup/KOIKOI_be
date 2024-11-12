package com.example.SWP.API;

import com.example.SWP.Service.KoiOrderService;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.entity.OrderDetails;
import com.example.SWP.model.request.OrderCancelRequest;
import com.example.SWP.model.request.OrderCreationRequest;
import com.example.SWP.model.request.RejectRequest;
import com.example.SWP.model.response.OrderDetailsResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

    @PostMapping("/consignOrder")
    public ResponseEntity createOrder2(@RequestParam long id,@RequestBody OrderCreationRequest orderCreationRequest){
        return ResponseEntity.ok(koiOrderService.convertConsignToOrder(id, orderCreationRequest));
    }


    @GetMapping("showKoiOrder")
    public ResponseEntity getOrderByAccountById(){
        return ResponseEntity.ok(koiOrderService.getKoiOrderByAccountId());
    }

    @GetMapping("all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(koiOrderService.getAllOrders());
    }

    @GetMapping("pending")
    public ResponseEntity getPendingOrders() {
        return ResponseEntity.ok(koiOrderService.getPendingOrders1());
    }

    @GetMapping("pending2")
    public ResponseEntity getPendingOrders2() {
        return ResponseEntity.ok(koiOrderService.getPendingOrders2());
    }

    @GetMapping("paid")
    public ResponseEntity getPaidOrders() {
        return ResponseEntity.ok(koiOrderService.getPaidOrders());
    }

    @GetMapping("buy")
    public ResponseEntity getBuy(){
        return ResponseEntity.ok(koiOrderService.koiOrdersBuys());
    }

    @GetMapping("consign")
    public ResponseEntity getConsign(){
        return ResponseEntity.ok(koiOrderService.koiOrdersConsigns());
    }


    @GetMapping("confirmed")
    public ResponseEntity getConfirmedOrders() {
        return ResponseEntity.ok(koiOrderService.getConfirmedOrders());
    }

    @GetMapping("confirmed2")
    public ResponseEntity getConfirmedOrders2() {
        return ResponseEntity.ok(koiOrderService.getConfirmedOrder2());
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity cancelOrder(@PathVariable long id, @RequestBody OrderCancelRequest request) {
        return ResponseEntity.ok(koiOrderService.cancelOrder(id, request));
    }
    @GetMapping("cancel")
    public ResponseEntity getCancelList(){
        return ResponseEntity.ok(koiOrderService.getCancelList());
    }

    @PutMapping("reject/{id}")
    public ResponseEntity cancelOrder(@PathVariable long id, @RequestBody RejectRequest request){
        return ResponseEntity.ok(koiOrderService.rejectOrder(id,request));
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

    @GetMapping("/details")
    public ResponseEntity  viewOrderDetails(@RequestParam long orderId) {
        Set<OrderDetailsResponse> orderDetails = koiOrderService.viewOrderDetails(orderId);
        return ResponseEntity.ok(orderDetails);
    }

}



