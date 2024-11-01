package com.example.SWP.Service;

import com.example.SWP.Enums.CancelOrderStatus;
import com.example.SWP.Enums.OrderStatus;
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

        List<OrderDetails> orderDetailsSet = koiOrder.getOrderDetails();

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

            // Check if getOrderDetails() is null before accessing getKoi()
            if (consignmentDetails1.getConsignment().getOrderDetails() != null) {
                orderDetail.setKoi(consignmentDetails1.getConsignment().getOrderDetails().getKoi());
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
        return orderRepository.findByOrderStatus(OrderStatus.PENDING);
    }

    public List<KoiOrder> getPaidOrderes(){
        return orderRepository.findByOrderStatus(OrderStatus.PAID);
    }

    public List<KoiOrder> getConfirmedOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
    }

    public List<KoiOrder> getAllOrders() {
        return orderRepository.findAll();
    }


//    public KoiOrder getOrderById(long id) {
//        Account account = accountUtils.getCurrentAccount();
//        KoiOrder order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
//        List<OrderDetails> orderItems = orderDetailsRepository.findByKoiOrderId(id);
//        Set<OrderDetails> orderDetails = new HashSet<>(orderItems);
//        for(OrderDetails item: orderDetails) {
//            System.out.println(item.getId());
//        }
//        order.setOrderDetails(orderDetails);
//        return order;
//    }

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

    public KoiOrder rejectOrder(long orderId, RejectRequest request){
        Account account = accountUtils.getCurrentAccount();
        KoiOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng ko tồn tại"));
        order.setOrderStatus(OrderStatus.REJECT);
        order.setNote(request.getNote());
        accountRepository.save(account);
        return orderRepository.save(order);
    }

    @Scheduled(fixedRate = 900000) // Chạy mỗi 15 phút (900000 milliseconds)
    @Transactional // Đảm bảo phiên Hibernate mở trong quá trình xử lý
    public void autoCancelUnpaidOrders() {
        // Tìm các đơn hàng có trạng thái PENDING và ngày xử lý cũ hơn 15 phút
        List<KoiOrder> unpaidOrders = orderRepository.findAllByOrderStatusAndProcessingDateLessThan(
                OrderStatus.PENDING,
                new Date(System.currentTimeMillis() + 900000)); // Thời gian 15 phút sau


        for (KoiOrder order : unpaidOrders) {
            // Kiểm tra xem đơn hàng chưa được thanh toán
            if (!OrderStatus.PAID.equals(order.getOrderStatus()) &&
                    !OrderStatus.CONFIRMED.equals(order.getOrderStatus())) {
                // Cập nhật trạng thái đơn hàng thành CANCELED
                order.setOrderStatus(OrderStatus.CANCELED);

                // Tạo yêu cầu hủy đơn hàng và đặt lý do
                CanceledOrder canceledOrder = new CanceledOrder();
                canceledOrder.setReason("THỜI GIAN THANH TOÁN HẾT HẠN");
                canceledOrder.setAccount(order.getAccount());
                canceledOrder.setTotalAmount(order.getTotalAmount());
                canceledOrder.setCancelDate(new Date());
                canceledOrder.setCancelOrderStatus(CancelOrderStatus.FINISHED);
                canceledOrder.setKoiOrder(order);

                // Lưu trạng thái mới của đơn hàng và yêu cầu hủy vào cơ sở dữ liệu
                orderRepository.save(order);
                cancelOrderRepository.save(canceledOrder);
            }
        }
    }

    public List<CanceledOrder> getListCancelOrders(){
        return cancelOrderRepository.findAll();
    }
}
