package com.example.SWP.API;


import com.example.SWP.Service.CartService;

import com.example.SWP.entity.Cart;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/Cart")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CartAPI {

    @Autowired
    CartService cartService;


    @PostMapping("{id}")
    public ResponseEntity addToCart(@PathVariable long id) {

        return ResponseEntity.ok(cartService.addToCart(id));
    }


    @GetMapping("/total")
    public ResponseEntity<Cart> getCartTotal(@RequestParam Long cartId,
                                             @RequestParam(required = false) UUID voucherCode) {
        Cart cartTotal = cartService.getCartTotal(cartId, voucherCode);
        return ResponseEntity.ok(cartTotal);
        }

    @GetMapping
    public ResponseEntity getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping("{id}")
    public String deleteCartItemById(@PathVariable long id) {
        cartService.delete(id);
        return "Delete successfully";
    }

    @PutMapping("increase/{id}")
    public ResponseEntity addCartItem(@PathVariable long id) {
        return ResponseEntity.ok(cartService.add(id));
    }

    @PutMapping("decrease/{id}")
    public ResponseEntity updateQuantityCartItem(@PathVariable long id) {
        return ResponseEntity.ok(cartService.removeOneProduct(id));
    }





}




