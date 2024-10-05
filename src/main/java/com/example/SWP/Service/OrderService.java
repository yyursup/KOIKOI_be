package com.example.SWP.Service;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.OrderRepository;
import com.example.SWP.Repository.VoucherRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.entity.Voucher;
import com.example.SWP.model.OrderRequest;
import com.example.SWP.model.VoucherRequestForDiscount;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    AccountUtils accountUtils;





    public OrderRequest createOrder(OrderRequest  orderRequest){
        KoiOrder koiOrder = modelMapper.map(orderRequest,KoiOrder.class);

        try{
            Account account = accountUtils.getCurrentAccount();
            koiOrder.setAccount(account);
            KoiOrder newKoi = orderRepository.save(koiOrder);
            return modelMapper.map(newKoi,OrderRequest.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public OrderRequest applyVoucher(Long orderID, UUID voucherCode){
        KoiOrder koiOrder = null;
        Voucher voucher = null;

        try{

            Optional<KoiOrder> koiOrder1 = orderRepository.findById(orderID);
            if (koiOrder1.isPresent()){
                koiOrder = koiOrder1.get();
            }else{
                System.out.println("Order can not found");;
            }

            Optional<Voucher> voucher1 = voucherRepository.findByCode(voucherCode);
            if(voucher1.isPresent()){
                voucher = voucher1.get();
            }else{
                System.out.println("Voucher can not found");
            }
        }catch (Exception e){
            throw new RuntimeException("ERROR");
        }

        if (voucher.getIs_active().equals("limited") &&
                voucher.getStart_date().isBefore(LocalDateTime.now()) &&
                voucher.getEnd_date().isAfter(LocalDateTime.now())) {

            double discount = koiOrder.getSubTotal() * voucher.getDiscount_amount();
            double newTotalAmount = koiOrder.getSubTotal() - discount + koiOrder.getShippingPee();
            koiOrder.setTotalAmount(newTotalAmount);
            modelMapper.map(voucher, VoucherRequestForDiscount.class);

            koiOrder.setVoucher(voucher);
            Account account = accountUtils.getCurrentAccount();
            koiOrder.setAccount(account);

             orderRepository.save(koiOrder);
             return modelMapper.map(koiOrder,OrderRequest.class);
        }else{
            throw new RuntimeException("Voucher is not valid or expired");
        }
    }

}
