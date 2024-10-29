package com.example.SWP.Service;

import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Repository.OrderRepository;
import com.example.SWP.entity.*;

import com.example.SWP.model.request.OrderCreationRequest;
import com.example.SWP.model.request.OrderPayRequest;
import com.example.SWP.model.response.KoiOrderResponse;
import com.example.SWP.utils.AccountUtils;
import jdk.jfr.BooleanFlag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class PaymentService {



    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountUtils accountUtils;

    Long lastPaidOrderId;



    public String createUrl(Long koiOrderId, OrderPayRequest request) throws  Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        Account account = accountUtils.getCurrentAccount();
        KoiOrder koiOrder = orderRepository.findById(koiOrderId)
                .orElseThrow(() -> new RuntimeException("Can not found this order"));
        //KoiOrder koiOrder = orderRepository.findKoiOrderById(koiOrderId);
        double money = koiOrder.getTotalAmount() * 100;
        String amount = String.valueOf((int) money);
        koiOrder.setCountry(request.getCountry() != null && !request.getCountry().isEmpty() ? request.getCountry() : account.getCountry());
        koiOrder.setCity(request.getCity() != null && !request.getCity().isEmpty() ? request.getCity() : account.getCity());
        koiOrder.setAddress(request.getAddress() != null && !request.getAddress().isEmpty() ? request.getAddress() : account.getSpecific_address());
        koiOrder.setPhone(request.getPhone() != null && !request.getPhone().isEmpty() ? request.getPhone() : account.getPhone_number());
        koiOrder.setEmail(request.getGmail() != null && !request.getGmail().isEmpty() ? request.getGmail() : account.getEmail());

        koiOrder.setOrderStatus(OrderStatus.PAID);

        orderRepository.save(koiOrder);

        lastPaidOrderId = koiOrder.getId();

        String tmnCode = "T77TVJXG";
        String secretKey = "9P81P2EVHRIN0FTELZPCMURONQFHON7I";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:5173/orderSuccess?orderId=" + koiOrderId;



        String currCode = "VND";
        String vnpTxnRef = UUID.randomUUID().toString();

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef",vnpTxnRef );
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + koiOrder.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount",amount);

        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }
    public KoiOrderResponse getLastPaidOrder() {
        if (lastPaidOrderId != null) {
            KoiOrder lastPaidOrder = orderRepository.findById(lastPaidOrderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
            return new KoiOrderResponse(
                    lastPaidOrder.getId(),
                    lastPaidOrder.getTotalAmount(),
                    lastPaidOrder.getShippingPee(),
                    lastPaidOrder.getCountry(),
                    lastPaidOrder.getCity(),
                    lastPaidOrder.getAddress(),
                    lastPaidOrder.getPhone(),
                    lastPaidOrder.getOrderDate(),
                    lastPaidOrder.getFullName(),
                    lastPaidOrder.getEmail(),
                    lastPaidOrder.getOrderStatus());
        } else {
            throw new RuntimeException("Chưa có đơn hàng nào được thanh toán gần đây");
        }
    }
    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}