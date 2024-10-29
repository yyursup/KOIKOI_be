package com.example.SWP.Service;

import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;


import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        List<CartDetails> cartDetail = cart.getActiveCartDetails();

        if (cartDetail.isEmpty()) {
            CartDetails cartDetails = new CartDetails();
            cartDetails.setImage(koi.getImage());
            cartDetails.setDescription(koi.getDescription());
            cartDetails.setName(koi.getName());
            cartDetails.setPrice(koi.getPrice());
            cartDetails.setQuantity(1);
            cartDetails.setKoi(koi);
            cartDetails.setCart(cart);
            cart.getCartDetails().add(cartDetails);

            // Cập nhật giá trị subtotal, shipping fee, và total amount
            double subTotal = calculateCartSubTotal(cart);
            double shippingFee = 100000;
            cart.setSubTotal(subTotal);
            cart.setShippingPee(shippingFee);
            cart.setTotalAmount(subTotal + shippingFee);

            return cartRepository.save(cart);
        } else {
            for (CartDetails details : cartDetail) {
                if (details.getKoi().getId() == id) {
                    if (details.getQuantity() >= koi.getQuantity()) {
                        throw new RuntimeException("The quantity in stock is not enough");
                    }
                    details.setQuantity(details.getQuantity() + 1);
                    cartDetailsRepository.save(details);
                    double subTotal = calculateCartSubTotal(cart);
                    double shippingFee = 100000;
                    cart.setSubTotal(subTotal);
                    cart.setShippingPee(shippingFee);
                    cart.setTotalAmount(subTotal + shippingFee);

                    return cartRepository.save(cart);
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

            double subTotal = calculateCartSubTotal(cart);
            double shippingFee = 100000;
            cart.setSubTotal(subTotal);
            cart.setShippingPee(shippingFee);
            cart.setTotalAmount(subTotal + shippingFee);

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


            if (voucher.getIs_active().equals("true") &&
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

    public CartDetails delete(long id) {
        CartDetails cartDetails = cartDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        Cart cart = cartDetails.getCart();
        cart.getCartDetails().remove(cartDetails);

        cartDetailsRepository.deleteCartItem(id);

        double subTotal = calculateCartSubTotal(cart);


        cart.setSubTotal(subTotal);
        cart.setTotalAmount(subTotal);
        cart.setShippingPee(0);

        cartRepository.save(cart);
        return cartDetails;
    }

    private double calculateCartSubTotal(Cart cart) {
        double subTotal = 0;
        for (CartDetails details : cart.getActiveCartDetails()) {
            subTotal += details.getKoi().getPrice() * details.getQuantity();
        }
        return subTotal;
    }

    public CartDetails add(long id) {
        CartDetails cartDetails = cartDetailsRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if(cartDetails.getQuantity() >= cartDetails.getKoi().getQuantity()) {
            throw new RuntimeException("Trong kho hàng không đủ");
        }
        Cart cart = cartDetails.getCart();
        double shippingFee = 100000;
        cartDetails.setQuantity(cartDetails.getQuantity() + 1);
        double subTotal = calculateCartSubTotal(cart);
        cart.setSubTotal(subTotal);
        cart.setTotalAmount(subTotal + shippingFee);
        cartRepository.save(cart);
        cartDetailsRepository.save(cartDetails);

        return cartDetails;
    }


    public CartDetails removeOneProduct(long id) {
        CartDetails cartItem = cartDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // Kiểm tra số lượng tồn kho
        if (cartItem.getQuantity() <= 1) {
            throw new RuntimeException("Số lượng sản phẩm không hợp lệ");
        }

        Cart cart = cartItem.getCart();
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        double subTotal = calculateCartSubTotal(cart);
        double shippingFee = 100000;
        cart.setSubTotal(subTotal);
        cart.setTotalAmount(subTotal + shippingFee);
        cartRepository.save(cart);
        cartDetailsRepository.save(cartItem);

        return cartItem;
    }
}
