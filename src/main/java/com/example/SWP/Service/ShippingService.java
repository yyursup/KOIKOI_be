package com.example.SWP.Service;

import com.example.SWP.Enums.StatusOrderDetails;
import com.example.SWP.Enums.StatusShipping;
import com.example.SWP.Repository.OrderDetailsRepository;
import com.example.SWP.Repository.OrderRepository;
import com.example.SWP.Repository.ShippingRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.entity.OrderDetails;
import com.example.SWP.entity.Shipping;
import com.example.SWP.model.request.ShippingRequest;
import com.example.SWP.model.response.ViewProfileResponse;
import com.example.SWP.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingService {



    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;


        public Shipping createShipping(long koiOrderId, ShippingRequest request){
        KoiOrder koiOrder = orderRepository.findById(koiOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Shipping shipping = new Shipping();

        shipping.setCodeShipping(request.getShippingCode());
        shipping.setDeliveryDate(request.getDeliveryDate());
        shipping.setDeliveredDate(request.getDeliveredDate());
        shipping.setAddress(koiOrder.getAddress());
        shipping.setPhone(koiOrder.getPhone());
        shipping.setToTalAmount(koiOrder.getTotalAmount());
        shipping.setName(koiOrder.getFullName());
        shipping.setShippingPee(koiOrder.getShippingPee());
        shipping.setShipping(StatusShipping.DELIVERY);
        shipping.setKoiOrder(koiOrder);
        koiOrder.getOrderDetails().forEach(details -> details.setStatus(StatusOrderDetails.DELIVERY));
        orderRepository.save(koiOrder);
        shippingRepository.save(shipping);
        return shipping;
    }

    public Shipping createShipping2(long orderDetailsId, ShippingRequest request){
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> new RuntimeException("OrderDetails not found"));
        KoiOrder koiOrder = orderDetails.getKoiOrder();

        Shipping shipping = new Shipping();

        shipping.setCodeShipping(request.getShippingCode());
        shipping.setDeliveryDate(request.getDeliveryDate());
        shipping.setDeliveredDate(request.getDeliveredDate());
        shipping.setAddress(koiOrder.getAddress());
        shipping.setPhone(koiOrder.getPhone());
        shipping.setToTalAmount(koiOrder.getOrderDetails().iterator().next().getPrice());
        shipping.setName(koiOrder.getFullName());
        shipping.setShippingPee(koiOrder.getShippingPee());
        shipping.setShipping(StatusShipping.DELIVERY);
        shipping.setKoiOrder(koiOrder);
        orderDetails.setStatus(StatusOrderDetails.DELIVERY);
        orderDetailsRepository.save(orderDetails);
        shippingRepository.save(shipping);
        return shipping;
    }

    public List<Shipping> searchShippingOrderByCode(String shippingCode){
        return shippingRepository.findShippingByCodeShipping(shippingCode);
    }

    public List<Shipping> getAllShipping() {
        List<Shipping> shippingList = shippingRepository.findAll();
        return shippingList;
    }

    public List<Shipping> getAllShippingsByAccount() {
        Account account = accountUtils.getCurrentAccount();
        return shippingRepository.findByKoiOrderAccountId(account.getId());
    }

    public Shipping conFirmShipping(Long id){
        Shipping shipping = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found"));
        shipping.getKoiOrder().getOrderDetails().forEach(orderDetail -> {
            orderDetail.setStatus(StatusOrderDetails.DELIVERED);
        });

        shipping.setShipping(StatusShipping.DELIVERED);
        orderRepository.save(shipping.getKoiOrder());
        shippingRepository.save(shipping);
        return shipping;
    }



}
