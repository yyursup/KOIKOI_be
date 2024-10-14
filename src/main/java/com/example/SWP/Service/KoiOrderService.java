package com.example.SWP.Service;

import com.example.SWP.Enums.CancelOrderStatus;
import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.Type;
import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;
import com.example.SWP.model.Request.OrderCancelRequest;
import com.example.SWP.model.Request.OrderCreationRequest;
import com.example.SWP.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
@Service
public class KoiOrderService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    OrderRepository orderRepository;


    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CancelOrderRepository cancelOrderRepository;



    @Transactional
    public KoiOrder convertCartToOrder(OrderCreationRequest request) {
        Account account = accountUtils.getCurrentAccount();
        Cart cart = account.getCart();
        Set<CartDetails> cartDetails = cart.getCartDetails();
        List<OrderDetails> orderDetails = new ArrayList<>();
        if (cartDetails.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        KoiOrder koiOrder = new KoiOrder();
        koiOrder.setAccount(account);
        koiOrder.setFullName(account.getFullName());
        koiOrder.setPhone(account.getPhone_number());
        koiOrder.setOrderDate(LocalDateTime.now());
        koiOrder.setOrderStatus(OrderStatus.PENDING);
        koiOrder.setAddress(request.getAddress());
        koiOrder.setPhone(request.getPhone());
        koiOrder.setNote(request.getNote());
        koiOrder.setTotalAmount(cart.getTotalAmount());
        koiOrder.setShippingPee(cart.getShippingPee());
        koiOrder.setType(Type.BUY);
        koiOrder = orderRepository.save(koiOrder);


        for (CartDetails cartDetailsItem : cartDetails) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setKoi(cartDetailsItem.getKoi());
            orderDetail.setQuantity(cartDetailsItem.getQuantity());
            orderDetail.setPrice(cartDetailsItem.getPrice());
            orderDetail.setKoiOrder(koiOrder);
            orderDetails.add(orderDetail);
        }

            orderDetailsRepository.saveAll(orderDetails);

            cart.getCartDetails().clear();

            cartRepository.save(cart);
            return koiOrder;
        }



    public List<KoiOrder> getKoiOrderByAccountId(){
        Account account = accountUtils.getCurrentAccount();
        List<KoiOrder> koiOrderList = orderRepository.findAccountById(account.getId());
        return koiOrderList;
    }

    public List<KoiOrder> getPendingOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.PENDING);
    }

    public List<KoiOrder> getConfirmedOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
    }

    public List<KoiOrder> getProcessingOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.PROCESSING);
    }

    public List<KoiOrder> getShippedOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.SHIPPED);
    }

    public List<KoiOrder> getDeliveredOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.DELIVERED);
    }

    public List<KoiOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public KoiOrder getOrderById(long id) {
        KoiOrder order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        List<OrderDetails> orderItems = orderDetailsRepository.findByKoiOrderId(id);
        Set<OrderDetails> orderDetails = new HashSet<>(orderItems);
        for(OrderDetails item: orderDetails) {
            System.out.println(item.getId());
        }
        order.setOrderDetails(orderDetails);
        return order;
    }

    public KoiOrder cancelOrder(long orderId, OrderCancelRequest request){
        Account account = accountUtils.getCurrentAccount();

        KoiOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng ko tồn tại"));
        CanceledOrder canceledOrder = new CanceledOrder();
        order.setOrderStatus(OrderStatus.CANCELED);
        Set<OrderDetails> orderDetails = order.getOrderDetails();

        for(OrderDetails item : orderDetails){
            Koi koi = item.getKoi();
        }

        canceledOrder.setCancelOrderStatus(CancelOrderStatus.PENDING);
        canceledOrder.setTotalAmount(order.getTotalAmount());
        canceledOrder.setAccount(order.getAccount());
        canceledOrder.setReason(request.getCanceledNote());
        canceledOrder.setCancelDate(LocalDate.now());
        canceledOrder.setKoiOrder(order);
        accountRepository.save(account);
        cancelOrderRepository.save(canceledOrder);
        return orderRepository.save(order);
    }

    public CanceledOrder refund(long id) {
        CanceledOrder canceledOrder = cancelOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        canceledOrder.setCancelOrderStatus(CancelOrderStatus.FINISHED);
        return cancelOrderRepository.save(canceledOrder);
    }

    public List<CanceledOrder> getListCancelOrders(){
        return cancelOrderRepository.findAll();
    }











}
