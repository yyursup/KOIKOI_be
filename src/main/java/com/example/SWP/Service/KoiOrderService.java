package com.example.SWP.Service;

import com.example.SWP.Enums.CancelOrderStatus;
import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.StatusOrderDetails;
import com.example.SWP.Enums.Type;
import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;

import com.example.SWP.model.request.OrderCancelRequest;
import com.example.SWP.model.request.OrderCreationRequest;
import com.example.SWP.model.request.RejectRequest;
import com.example.SWP.model.response.OrderDetailsResponse;
import com.example.SWP.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        List<CartDetails> cartDetails = cart.getActiveCartDetails();
        List<OrderDetails> orderDetails = new ArrayList<>();
        if (cartDetails.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        KoiOrder koiOrder = new KoiOrder();
        koiOrder.setAccount(account);
        koiOrder.setFullName(account.getUsername());
        koiOrder.setPhone(account.getPhone_number());
        koiOrder.setOrderDate(new Date());
        koiOrder.setOrderStatus(OrderStatus.PENDING);
        koiOrder.setAddress(account.getSpecific_address());
        koiOrder.setPhone(request.getPhone());
        koiOrder.setCity(account.getCity());
        koiOrder.setCountry(account.getCountry());
        koiOrder.setNote(request.getNote());
        koiOrder.setTotalAmount(cart.getTotalAmount());
        koiOrder.setShippingPee(cart.getShippingPee());
        koiOrder.setProcessingDate(new Date());
        koiOrder.setType(Type.BUY);
        koiOrder = orderRepository.save(koiOrder);

        for (CartDetails cartDetailsItem : cartDetails) {
            cartDetailsItem.setDeleted(true);
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setName(cartDetailsItem.getName());
            orderDetail.setKoi(cartDetailsItem.getKoi());
            orderDetail.setQuantity(cartDetailsItem.getQuantity());
            orderDetail.setPrice(cartDetailsItem.getPrice());
            orderDetail.setImage(cartDetailsItem.getImage());
            orderDetail.setKoiOrder(koiOrder);
            orderDetails.add(orderDetail);
        }

            orderDetailsRepository.saveAll(orderDetails);

            cart.getCartDetails().clear();

            cartRepository.save(cart);
            return koiOrder;
    }

    public Set<OrderDetailsResponse> viewOrderDetails(Long orderId) {

        KoiOrder koiOrder = orderRepository.findKoiOrderById(orderId);
        if (koiOrder == null) {
            throw new RuntimeException("Order not found with id " + orderId);
        }

        Set<OrderDetails> orderDetailsSet = koiOrder.getOrderDetails();

        if (orderDetailsSet.isEmpty()) {
            throw new RuntimeException("No order details found for order id " + orderId);
        }
        Set<OrderDetailsResponse> orderDetailsResponseSet = orderDetailsSet.stream()
                .map(orderDetails -> new OrderDetailsResponse(
                        orderDetails.getId(),
                        orderDetails.getName(),
                        orderDetails.getPrice(),
                        orderDetails.getQuantity(),
                        orderDetails.getImage(),
                        orderDetails.getStatus()))
                .collect(Collectors.toSet());

        return orderDetailsResponseSet;
    }

    public KoiOrder convertConsignToOrder(long consignmentId, OrderCreationRequest request) {
        // Lấy tài khoản hiện tại
        Account account = accountUtils.getCurrentAccount();

        // Lấy consignment dựa trên ID được truyền vào
        Consignment consignment = account.getConsignments().stream()
                .filter(c -> c.getId() == consignmentId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Consignment not found"));

        Set<ConsignmentDetails> consignmentDetails = consignment.getConsignmentDetailsSet();
        if (consignmentDetails.isEmpty()) {
            throw new RuntimeException("Consignment has no details");
        }

        KoiOrder koiOrder = new KoiOrder();
        koiOrder.setAccount(account);
        koiOrder.setFullName(account.getUsername());
        koiOrder.setPhone(account.getPhone_number());
        koiOrder.setOrderDate(new Date());
        koiOrder.setOrderStatus(OrderStatus.PENDING);
        koiOrder.setAddress(request.getAddress());
        koiOrder.setPhone(request.getPhone());
        koiOrder.setNote(consignment.getNote());
        koiOrder.setTotalAmount(consignment.getTotalAmount());
        koiOrder.setProcessingDate(new Date());
        koiOrder.setConsignment(consignment);
        koiOrder.setType(Type.CONSIGN);


        koiOrder = orderRepository.save(koiOrder);

        List<OrderDetails> orderDetails = new ArrayList<>();
        for (ConsignmentDetails consignmentDetails1 : consignmentDetails) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setName(consignmentDetails1.getName());
            orderDetail.setQuantity(consignmentDetails1.getQuantity());
            orderDetail.setPrice(consignmentDetails1.getPrice());
            orderDetail.setImage(consignmentDetails1.getImage());
            orderDetail.setKoi(consignmentDetails1.getKoi());
            orderDetail.setStatus(StatusOrderDetails.CONSIGN);


            // Check if getOrderDetails() is null before accessing getKoi()
            if (consignmentDetails1.getConsignment().getKoiOrder().getOrderDetails() != null) {
                orderDetail.setKoi(consignmentDetails1.getConsignment().getKoiOrder().getOrderDetails().iterator().next().getKoi());
            } else {
                // Handle the case where getOrderDetails() is null
                System.out.println("Warning: OrderDetails is null for ConsignmentDetails with ID " + consignmentDetails1.getId());
                // Set a default Koi or handle as necessary
            }

            orderDetail.setKoiOrder(koiOrder); // Gán mối quan hệ với KoiOrder
            orderDetails.add(orderDetail);
        }

        orderDetailsRepository.saveAll(orderDetails);

        return koiOrder;
    }

     public List<KoiOrder> getKoiOrderByAccountId(){
     Account account = accountUtils.getCurrentAccount();
     List<KoiOrder> koiOrderList = orderRepository.findAllOrdersByAccountId(account.getId());
     return koiOrderList;
     }

    public List<KoiOrder> getPendingOrders() {
        return orderRepository.findByOrderStatusAndType(OrderStatus.PENDING,Type.BUY);
    }

    public List<KoiOrder> getPendingOrders2() {
        return orderRepository.findByOrderStatusAndType(OrderStatus.PENDING,Type.CONSIGN);
    }

    public List<KoiOrder> getPaidOrders(){
        return orderRepository.findByOrderStatusAndType(OrderStatus.PAID,Type.BUY);
    }

    public List<KoiOrder> getConfirmedOrders() {
        return orderRepository.findByOrderStatusAndType(OrderStatus.CONFIRMED,Type.BUY);
    }

    public List<KoiOrder> getConfirmedOrders2() {
        return orderRepository.findByOrderStatusAndType(OrderStatus.CONFIRMED,Type.CONSIGN);
    }

    public List<KoiOrder> getConfirmOrders(){
        return orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
    }

    public List<KoiOrder> getCancelList(){
        return orderRepository.findByOrderStatus(OrderStatus.CANCELED);
    }



    public List<KoiOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<KoiOrder> koiOrdersBuy(){

        return orderRepository.findKoiOrderByType(Type.BUY);
    }

    public List<KoiOrder> koiOrdersConsign(){
        return orderRepository.findKoiOrderByType(Type.CONSIGN);
    }


    public KoiOrder getOrderById(long id) {
        Account account = accountUtils.getCurrentAccount();
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
        order.setOrderStatus(OrderStatus.CANCELED);
        CanceledOrder canceledOrder = new CanceledOrder();
        canceledOrder.setCancelOrderStatus(CancelOrderStatus.FINISHED);
        canceledOrder.setOrderStatus(OrderStatus.CANCELED);
        canceledOrder.setTotalAmount(order.getTotalAmount());
        canceledOrder.setAccount(order.getAccount());
        canceledOrder.setEmail(account.getEmail());
        canceledOrder.setReason(request.getNote());
        canceledOrder.setCancelDate(new Date());
        canceledOrder.setKoiOrder(order);
        accountRepository.save(account);
        cancelOrderRepository.save(canceledOrder);
        return orderRepository.save(order);
    }

    public KoiOrder rejectOrder(long orderId, RejectRequest request){
        Account account = accountUtils.getCurrentAccount();
        KoiOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng ko tồn tại"));
        order.setOrderStatus(OrderStatus.REJECT);
        order.setNote(request.getNote());
        accountRepository.save(account);
        return orderRepository.save(order);
    }

    public List<CanceledOrder> getListCancelOrders(){
        return cancelOrderRepository.findAll();
    }

}
