package com.example.SWP.Service;

import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;
import com.example.SWP.model.CartResponse;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    CartDetailsRepository cartDetailsRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    KoiRepository koiRepository;





//    public CartResponse addToCart(long id, UUID voucherCode)
    public String addToCart(long id){
        Account account = accountUtils.getCurrentAccount();
        Cart cart = account.getCart();
        Koi koi = koiRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        Set<CartDetails> cartDetails = cart.getCartDetails();
        if(cartDetails.isEmpty()){
            CartDetails cartDetails1 = new CartDetails();
            cartDetails1.setImage(koi.getImage());
            cartDetails1.setPrice(koi.getPrice());
            cartDetails1.setQuantity(1);
            cartDetails1.setKoi(koi);
            cartDetails1.setCart(cart);
            cartDetailsRepository.save(cartDetails1);
        } else {
          for (CartDetails details : cartDetails){
              if(details.getKoi().getId() == id){
                  if(details.getQuantity() >= details.getQuantity()){
                      throw new RuntimeException("The quantity in stock is not enough");
                  }
                  details.setQuantity(details.getQuantity() + 1);
                  cartDetailsRepository.save(details);
                  return "Update quantity this product this cart";
              }
          }

          CartDetails cartDetails1 = new CartDetails();
          cartDetails1.setImage(koi.getImage());
          cartDetails1.setName(koi.getName());
          cartDetails1.setQuantity(1);
          cartDetails1.setPrice(koi.getPrice());
          cartDetails1.setKoi(koi);
          cartDetails1.setCart(cart);
          cartDetailsRepository.save(cartDetails1);
        }

        return "Add to cart successfully";

//        double subTotal = calculateCartSubTotal(cart);
//        double discount = 0;
//
//
//        if (voucherCode != null) {
//            Voucher voucher = voucherRepository.findByCode(voucherCode)
//                    .orElseThrow(() -> new RuntimeException("Voucher không hợp lệ"));
//            if (voucher.getIs_active().equals("limited") &&
//                    voucher.getStart_date().isBefore(LocalDateTime.now()) &&
//                    voucher.getEnd_date().isAfter(LocalDateTime.now())) {
//                    discount = subTotal * voucher.getDiscount_amount();
//            }
//        }
//
//        double shippingFee = 100000;
//        double totalAmount = subTotal - discount + shippingFee;
//
//        // Trả về kết quả giỏ hàng và tổng tiền
//        CartResponse response = new CartResponse();
//        response.setSubTotal(subTotal);
//        response.setDiscount(discount);
//        response.setShippingFee(shippingFee);
//        response.setTotalAmount(totalAmount);
//
//        return response;

    }

    public CartResponse getCartTotal(Long id, UUID voucherCode) {
        Account account = accountUtils.getCurrentAccount(); // phiên đang đăng nhập
        Cart cart = account.getCart();
        // lấy giỏ hàng từ tài khoản vì mỗi tài khoản có 1 giỏ hàng khác nhau


        double subTotal = calculateCartSubTotal(cart);


        double discount = 0;
        if (voucherCode != null) {
            Voucher voucher = voucherRepository.findByCode(voucherCode)
                    .orElseThrow(() -> new RuntimeException("Voucher cannot be found"));


            if (voucher.getIs_active().equals("limited") &&
                    voucher.getStart_date().isBefore(LocalDateTime.now()) &&
                    voucher.getEnd_date().isAfter(LocalDateTime.now())) {
                discount = subTotal * voucher.getDiscount_amount();
            }
        }


        double shippingFee = 100000;
        double totalAmount = subTotal - discount + shippingFee;


        CartResponse response = new CartResponse();
        response.setSubTotal(subTotal);
        response.setDisCount(discount);
        response.setShippingPee(shippingFee);
        response.setTotalAmount(totalAmount);


        return response;
    }

    public Cart getCart() {
        Account account = accountUtils.getCurrentAccount();
        return account.getCart();
    }

    public void delete(long id){
        cartDetailsRepository.deleteById(id);
    }


    private double calculateCartSubTotal(Cart cart) {
        double subTotal = 0;
        for (CartDetails details : cart.getCartDetails()) {
            subTotal += details.getKoi().getPrice() * details.getQuantity();
        }
        return subTotal;
    }

    public CartDetails add(long id) {
        CartDetails cartDetails = cartDetailsRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if(cartDetails.getQuantity() >= cartDetails.getKoi().getQuantity()) {
            throw new RuntimeException("Trong kho hàng không đủ");
        }
        cartDetails.setQuantity(cartDetails.getQuantity() + 1);
        return cartDetailsRepository.save(cartDetails);
    }

    public CartDetails removeOneProduct(long id) {
        CartDetails cartItem = cartDetailsRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if(cartItem.getQuantity() > cartItem.getKoi().getQuantity()) {
            throw new RuntimeException("Trong kho hàng không đủ");
        }
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        return cartDetailsRepository.save(cartItem);
    }
}
