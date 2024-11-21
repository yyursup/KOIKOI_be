package com.example.SWP.API;

import com.example.SWP.Enums.*;
import com.example.SWP.Repository.*;
import com.example.SWP.Service.EmailService;
import com.example.SWP.Service.PaymentService;
import com.example.SWP.entity.*;
import com.example.SWP.model.request.OrderCreationRequest;
import com.example.SWP.model.request.OrderPayRequest;
import com.example.SWP.model.response.KoiOrderResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/payment")
@SecurityRequirement(name="api")
@CrossOrigin("*")
public class PaymentAPI {

    @Autowired
    PaymentService paymentService;

    @Autowired
    EmailService emailService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    // Endpoint tạo URL thanh toán VNPay
    @PostMapping("/VNPay")
    public ResponseEntity create(@RequestParam Long koiOrderId, @RequestBody OrderPayRequest request) throws Exception {
        String vnPayURL = paymentService.createUrl(koiOrderId, request);
        return ResponseEntity.ok(vnPayURL);
    }

    // Endpoint lấy đơn hàng thanh toán gần nhất
    @GetMapping("/lastPaidOrder")
    public ResponseEntity getLastPaidOrder() {
        KoiOrderResponse response = paymentService.getLastPaidOrder();
        return ResponseEntity.ok(response);
    }

        @GetMapping("/response")
        public ResponseEntity<String> checkSuccess(@RequestParam String vnp_TxnRef,
                                                   @RequestParam String vnp_ResponseCode) {
            KoiOrder koiOrder = orderRepository.findByVnPayTxRef(vnp_TxnRef);

            if (koiOrder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng với VNPayTxnRef: " + vnp_TxnRef);
            }

            if ("00".equals(vnp_ResponseCode)) {

                Payment payment = new Payment();
                payment.setPaymentAmount(koiOrder.getTotalAmount());
                payment.setMethod(PaymentType.BANKING);
                payment.setPaymentDate(new Date());
                payment.setKoiOrder(koiOrder);
                paymentRepository.save(payment);

                Transactions transactions = new Transactions();
                transactions.setPayment(payment);
                transactions.setFrom(null);
                transactions.setTotalAmount(koiOrder.getTotalAmount());
                transactions.setTo(koiOrder.getAccount());
                transactions.setDescription("VNPAY TO CUSTOMER");
                transactions.setTransactionIdVNPay(koiOrder.getVnPayTxRef());
                transactions.setTransactionsDate(new Date());
                transactions.setStatus(TransactionsEnum.SUCCESS);
                transactionsRepository.save(transactions);


                Account account = koiOrder.getAccount();
                account.setBalance(account.getBalance() + koiOrder.getTotalAmount());

                koiOrder.setOrderStatus(OrderStatus.PAID);


                if (koiOrder.getConsignment() != null) {
                    Consignment consignment = consignmentRepository.findById(koiOrder.getConsignment().getId())
                            .orElseThrow(() -> new RuntimeException("Can not found"));

                        consignment.setStatus(StatusConsign.PAID);
                        consignmentRepository.save(consignment);
                }

                orderRepository.save(koiOrder);
                accountRepository.save(account);

                return ResponseEntity.ok("Thanh toán thành công. VNPayTxnRef: " + vnp_TxnRef);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Thanh toán thất bại với mã lỗi: " + vnp_ResponseCode);
            }
        }
}


