package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Koi;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Orders;
import com.example.demo.model.OrderDetailRequest;
import com.example.demo.model.OrderRequest;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    OrderRepository orderRepository;

    public Orders create(OrderRequest orderRequest) {
        Account customer = authenticationService.getCurrentAccount();
        Orders orders = new Orders();
        List<OrderDetail> orderDetails = new ArrayList<>();
        float total = 0;

        orders.setCustomer(customer);
        orders.setDate(new Date()); //lay ngay tgian hien tai

        for(OrderDetailRequest orderDetailRequest: orderRequest.getDetail()) {
            Koi koi = koiRepository.findKoiById(orderDetailRequest.getKoiId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setPrice(koi.getPrice());
            orderDetail.setOrders(orders);
            orderDetail.setKoi(koi);

            orderDetails.add(orderDetail);

            total += koi.getPrice() * orderDetailRequest.getQuantity();
        }
        orders.setOrderDetails(orderDetails);
        orders.setTotal(total);
        return orderRepository.save(orders);
    }

}
