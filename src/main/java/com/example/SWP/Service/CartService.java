package com.example.SWP.Service;

import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;


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


    public Cart addToCart(long id){
        Account account = accountUtils.getCurrentAccount();
        Cart cart = account.getCart();
        Koi koi = koiRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        Set<CartDetails> cartDetail = cart.getCartDetails();
        if(cartDetail.isEmpty()){
            CartDetails cartDetails = new CartDetails();
            cartDetails.setImage(koi.getImage());
            cartDetails.setDescription(koi.getDescription());
            cartDetails.setName(koi.getName());
            cartDetails.setPrice(koi.getPrice());
            cartDetails.setQuantity(1);
            cartDetails.setKoi(koi);
            cartDetails.setCart(cart);
            cart.getCartDetails().add(cartDetails);
            return cartRepository.save(cart);
        } else {
          for (CartDetails details : cartDetail){
              if(details.getKoi().getId() == id){
                  if(details.getQuantity() >= koi.getQuantity()){
                      throw new RuntimeException("The quantity in stock is not enough");
                  }
                  details.setQuantity(details.getQuantity() + 1);
                  cartDetailsRepository.save(details);
                  return cartRepository.findById(cart.getId()).get();
              }
          }

            CartDetails cartDetails = new CartDetails();
            cartDetails.setImage(koi.getImage());
            cartDetails.setDescription(koi.getDescription());
            cartDetails.setName(koi.getName());
            cartDetails.setPrice(koi.getPrice());
            cartDetails.setQuantity(1);
            cartDetails.setKoi(koi);
            cartDetails.setCart(cart);
            cart.getCartDetails().add(cartDetails);
            return cartRepository.save(cart);
        }
    }

    public Cart getCartTotal(Long id, UUID voucherCode) {
        Account account = accountUtils.getCurrentAccount(); // phiên đang đăng nhập
        Cart cart = account.getCart();
        // lấy giỏ hàng từ tài khoản vì mỗi tài khoản có 1 giỏ hàng khác nhau


        double subTotal = calculateCartSubTotal(cart);


        double discount = 0;
        Voucher voucher = voucherRepository.findVoucherById(id);
        if (voucherCode != null) {
             voucher = voucherRepository.findByCode(voucherCode)
                    .orElseThrow(() -> new RuntimeException("Voucher cannot be found"));


            if (voucher.getIs_active().equals("limited") &&
                    voucher.getStart_date().isBefore(LocalDateTime.now()) &&
                    voucher.getEnd_date().isAfter(LocalDateTime.now())) {
                discount = subTotal * voucher.getDiscount_amount();
            }
        }


        double shippingFee = 100000;
        double totalAmount = subTotal - discount + shippingFee;



        cart.setSubTotal(subTotal);
        cart.setShippingPee(shippingFee);
        cart.setTotalAmount(totalAmount);
        cart.setVoucher(voucher);
        return cartRepository.save(cart);


    }

    public Cart getCart() {
        Account account = accountUtils.getCurrentAccount();
        return account.getCart();
    }

    public void delete(long id){
        cartDetailsRepository.deleteCartItem(id);
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
