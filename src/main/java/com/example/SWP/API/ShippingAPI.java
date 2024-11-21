package com.example.SWP.API;

import com.example.SWP.Service.ShippingService;
import com.example.SWP.entity.Shipping;
import com.example.SWP.model.request.ShippingRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shipping")
@SecurityRequirement(name="api")
@CrossOrigin("*")
public class ShippingAPI {

    @Autowired
    ShippingService shippingService;


    @GetMapping("search")
    public ResponseEntity getOne(@RequestParam String codeShipping) {
        return ResponseEntity.ok(shippingService.searchShippingOrderByCode(codeShipping));
    }

    @PostMapping("create/{id}")
    public ResponseEntity createShipping(@PathVariable long id, @RequestBody ShippingRequest request){
        Shipping shipping = shippingService.createShipping(id, request);
        return ResponseEntity.ok(shipping);
    }

    @PostMapping("create2/{id}")
    public ResponseEntity createShipping2(@PathVariable long id, @RequestBody ShippingRequest request){
        Shipping shipping = shippingService.createShipping2(id, request);
        return ResponseEntity.ok(shipping);
    }

    @GetMapping("searchAll")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(shippingService.getAllShipping());
    }

    @GetMapping("/shippings")
    public ResponseEntity getAllShippingsByCurrentAccount() {
        List<Shipping> shippings = shippingService.getAllShippingsByAccount();
        return ResponseEntity.ok(shippings);
    }

    @PutMapping("/confirm")
    public ResponseEntity confirmShipping(@RequestParam long id){
        Shipping shipping = shippingService.conFirmShipping(id);
        return ResponseEntity.ok(shipping);
    }

}
