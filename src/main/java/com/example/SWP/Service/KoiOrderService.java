package com.example.SWP.Service;

import com.example.SWP.Enums.CancelOrderStatus;
import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.Type;
import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;

import com.example.SWP.model.request.OrderCancelRequest;
import com.example.SWP.model.request.OrderCreationRequest;
import com.example.SWP.model.response.OrderDetailsResponse;
import com.example.SWP.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        koiOrder.setProcessingDate(new Date());
        koiOrder.setType(Type.BUY);
        koiOrder = orderRepository.save(koiOrder);


        for (CartDetails cartDetailsItem : cartDetails) {
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
                        orderDetails.getImage()))
                .collect(Collectors.toSet());

        return orderDetailsResponseSet;
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
        order.setOrderStatus(OrderStatus.CANCELED);
        CanceledOrder canceledOrder = new CanceledOrder();
        canceledOrder.setCancelOrderStatus(CancelOrderStatus.FINISHED);
        canceledOrder.setTotalAmount(order.getTotalAmount());
        canceledOrder.setAccount(order.getAccount());
        canceledOrder.setReason(request.getNote());
        canceledOrder.setCancelDate(new Date());
        canceledOrder.setKoiOrder(order);
        accountRepository.save(account);
        cancelOrderRepository.save(canceledOrder);
        return orderRepository.save(order);
    }

    @Scheduled(fixedRate = 60000) // Chạy mỗi phút
    @Transactional // Đảm bảo phiên Hibernate mở trong quá trình xử lý
    public void autoCancelUnpaidOrders() {
        // Tìm các đơn hàng có trạng thái PENDING và quá hạn 1 phút
        List<KoiOrder> unpaidOrders = orderRepository.findAllByOrderStatusAndProcessingDateLessThan(OrderStatus.PENDING, new Date(System.currentTimeMillis() + 300000));

        for (KoiOrder order : unpaidOrders) {
            // Kiểm tra xem đơn hàng đã thanh toán hay chưa
            if (!OrderStatus.PAID.equals(order.getOrderStatus())) {
                // Cập nhật trạng thái đơn hàng thành CANCELED
                order.setOrderStatus(OrderStatus.CANCELED);

                // Tạo yêu cầu hủy đơn hàng và đặt lý do
                CanceledOrder canceledOrder = new CanceledOrder();
                canceledOrder.setReason("PAYMENT EXPIRED");
                canceledOrder.setAccount(order.getAccount());
                canceledOrder.setTotalAmount(order.getTotalAmount());
                canceledOrder.setCancelDate(new Date());
                canceledOrder.setCancelOrderStatus(CancelOrderStatus.FINISHED);
                canceledOrder.setKoiOrder(order);

                // Lưu trạng thái mới của đơn hàng vào cơ sở dữ liệu
                orderRepository.save(order);
                cancelOrderRepository.save(canceledOrder);
            }
        }
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
