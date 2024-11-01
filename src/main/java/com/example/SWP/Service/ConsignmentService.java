package com.example.SWP.Service;

import com.example.SWP.Enums.StatusConsign;
import com.example.SWP.Enums.TypeOfConsign;
import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;
import com.example.SWP.model.MailBody;
import com.example.SWP.model.request.KoiRequest;
import com.example.SWP.model.response.ConsignmentDetailsResponse;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ConsignmentService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    ConsignmentDetailsRepository consignmentDetailsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    KoiTypeRepository koiTypeRepository;

    public Consignment createConsignment(long orderDetailsId, LocalDate startDate, LocalDate endDate) {
        Account account = accountUtils.getCurrentAccount();

        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> new RuntimeException("OrderDetails not found"));

        // Kiểm tra nếu sản phẩm đã được ký gửi trước đó
        boolean isConsigned = consignmentRepository.existsByOrderDetails(orderDetails);
        if (isConsigned) {
            throw new RuntimeException("This product has already been consigned and cannot be consigned again.");
        }

        KoiOrder koiOrder = orderDetails.getKoiOrder(); // Lấy KoiOrder từ OrderDetails

        Consignment consignment = new Consignment();
        consignment.setStart_date(startDate);
        consignment.setEnd_date(endDate);
        consignment.setProduct_name(orderDetails.getName());
        consignment.setType(TypeOfConsign.CARE);
        consignment.setStatus(StatusConsign.VALID);
        consignment.setOrderDetails(orderDetails);

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween < 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        consignment.setNote("You consign this koi for : " + daysBetween + " days");
        consignment.setAccount(account);


        double subTotal = calculateSubTotal(orderDetails);
        double totalAmount = calculateTotalAmount(subTotal, daysBetween);

        consignment.setTotalAmount(totalAmount);


        ConsignmentDetails consignmentDetails = new ConsignmentDetails();
        consignmentDetails.setName(orderDetails.getName());
        consignmentDetails.setPrice(orderDetails.getPrice());
        consignmentDetails.setQuantity(orderDetails.getQuantity());
        consignmentDetails.setImage(orderDetails.getImage());
        consignmentDetails.setConsignment(consignment);
        consignmentDetails.setOrderDetails(orderDetails);
        consignment.getConsignmentDetailsSet().add(consignmentDetails);


        consignmentRepository.save(consignment);

        return consignment;
    }

    public Consignment createConsignmentForSell(KoiRequest koiRequest, Long koiTypeId) {
        Account account = accountUtils.getCurrentAccount();

        // Tạo Koi từ KoiRequest
        Koi koi = modelMapper.map(koiRequest, Koi.class);
        koi.setAccount(account);

        // Lấy KoiType và thiết lập cho Koi
        KoiType koiType = koiTypeRepository.findById(koiTypeId)
                .orElseThrow(() -> new RuntimeException("KoiType not found"));
        koi.setKoiType(koiType);
        koi.setCategory(koiType.getCategory());

        // Lưu Koi vào cơ sở dữ liệu
        try {
            koiRepository.save(koi);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save Koi", e);
        }

        // Tạo Consignment và liên kết với Account
        Consignment consignment = new Consignment();
        consignment.setType(TypeOfConsign.SELL);
        consignment.setAccount(account);// Liên kết Consignment với Account


        // Tạo ConsignmentDetails và liên kết với Koi và Consignment
        ConsignmentDetails consignmentDetails = new ConsignmentDetails();
        consignmentDetails.setName(koi.getName());
        consignmentDetails.setQuantity(koi.getQuantity());
        consignmentDetails.setPrice(koi.getPrice());
        consignmentDetails.setImage(koi.getImage());
        consignmentDetails.setKoi(koi); // Liên kết với Koi
        consignmentDetails.setConsignment(consignment); // Liên kết với Consignment

        // Thêm ConsignmentDetails vào Consignment
        consignment.getConsignmentDetailsSet().add(consignmentDetails);

        double totalAmount = consignmentDetails.getQuantity() * consignmentDetails.getPrice();
        consignment.setTotalAmount(totalAmount);
        consignment.setProduct_name(consignmentDetails.getName());
        consignment.setNote("I want to sell " + consignmentDetails.getQuantity() + " koi fish with price : " + consignmentDetails.getPrice());


        // Lưu Consignment vào cơ sở dữ liệu
        consignmentRepository.save(consignment);

        return consignment;
    }


    public Consignment cancelConsignment(long consignmentId, LocalDate cancelDate) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (cancelDate.isBefore(consignment.getStart_date()) || cancelDate.isAfter(consignment.getEnd_date())) {
            throw new RuntimeException("Cancel date must be within the consignment period");
        }
        long dayUsed = ChronoUnit.DAYS.between(consignment.getStart_date(), cancelDate);

        if (dayUsed < 0) {
            throw new RuntimeException("Cancel date must be after the start date");
        }
        long totalDays = ChronoUnit.DAYS.between(consignment.getStart_date(), consignment.getEnd_date());
        long unusedDays = totalDays - dayUsed;
        consignment.setEnd_date(cancelDate);
        consignment.setStatus(StatusConsign.CANCELLED);
        double subTotal = 0;
        for (ConsignmentDetails details : consignment.getConsignmentDetailsSet()) {
            subTotal += details.getPrice() * details.getQuantity();
        }
        double totalAmount = calculateTotalAmount(subTotal, unusedDays);
        consignment.setTotalAmount(totalAmount);
        consignment.setNote("Customer cancels koi care service in : " + cancelDate +
        "the system will refund: " + totalAmount);

        consignmentRepository.save(consignment);

        return consignment;

    }

    public Consignment extendConsignment(long consignmentId, LocalDate new_EndDate) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        LocalDate newStart_date = consignment.getEnd_date();

        if (new_EndDate.isBefore(newStart_date)) {
            throw new RuntimeException("New date must be after the end date");
        }
        long extensionDays = ChronoUnit.DAYS.between(newStart_date, new_EndDate);

        double subTotal = 0;
        for (ConsignmentDetails details : consignment.getConsignmentDetailsSet()) {
            subTotal += details.getPrice() * details.getQuantity();
        }
        double totalAmount = calculateTotalAmount(subTotal, extensionDays);

        consignment.setEnd_date(new_EndDate);
        consignment.setStatus(StatusConsign.VALID);
        consignment.setTotalAmount(totalAmount);
        consignment.setNote("Customer has extended the consignment for " + extensionDays + " more days");
        consignmentRepository.save(consignment);
        return consignment;
    }

    // Tính tổng giá trị đơn hàng (subtotal)
    private double calculateSubTotal(OrderDetails orderDetails) {
        return orderDetails.getQuantity() * orderDetails.getPrice();
    }

    private double calculateTotalAmount(double subTotal, long days) {
        double dailyFeeRate = 0.05;
        return subTotal * dailyFeeRate * days;
    }

    public ConsignmentDetails delete(long id) {
        ConsignmentDetails consignmentDetails = consignmentDetailsRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Not found"));

        Consignment consignment = consignmentDetails.getConsignment();
        consignment.getConsignmentDetailsSet().remove(consignmentDetails);

        consignmentDetailsRepository.deleteConsignmentDetails(id);

        updateConsignmentTotal(consignment);

        consignmentRepository.save(consignment);

        return consignmentDetails;
    }

    public ConsignmentDetails add(long id) {
        ConsignmentDetails consignmentDetails = consignmentDetailsRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if (consignmentDetails.getQuantity() >= consignmentDetails.getOrderDetails().getQuantity()) {
            throw new RuntimeException("Trong đơn hàng không đủ ");
        }
        consignmentDetails.setQuantity(consignmentDetails.getQuantity() + 1);
        Consignment consignment = consignmentDetails.getConsignment();
        updateConsignmentTotal(consignment);
        consignmentDetailsRepository.save(consignmentDetails);
        consignmentRepository.save(consignment);
        return consignmentDetails;
    }

    public ConsignmentDetails removeOneProduct(long id) {
        ConsignmentDetails consignmentDetails = consignmentDetailsRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if (consignmentDetails.getQuantity() > consignmentDetails.getOrderDetails().getQuantity()) {
            throw new RuntimeException("Trong đơn hàng không đủ");
        }
        consignmentDetails.setQuantity(consignmentDetails.getQuantity() - 1);
        Consignment consignment = consignmentDetails.getConsignment();
        updateConsignmentTotal(consignment);

        // Lưu lại ConsignmentDetails và Consignment
        consignmentDetailsRepository.save(consignmentDetails);
        consignmentRepository.save(consignment); // Lưu lại tổng giá trị mới của consignment

        return consignmentDetails;
    }

    private void updateConsignmentTotal(Consignment consignment) {

        if (consignment.getConsignmentDetailsSet() == null || consignment.getConsignmentDetailsSet().isEmpty()) {
            consignment.setTotalAmount(0);
            return;
        }

        double subTotal = 0;
        for (ConsignmentDetails details : consignment.getConsignmentDetailsSet()) {
            subTotal += details.getPrice() * details.getQuantity();
        }

        long daysBetween = ChronoUnit.DAYS.between(consignment.getStart_date(), consignment.getEnd_date());


        if (daysBetween < 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }


        double totalAmount = calculateTotalAmount(subTotal, daysBetween);


        consignment.setTotalAmount(totalAmount);
    }

    public List<Consignment> getConsignmentByAccountId(){
        Account account = accountUtils.getCurrentAccount();
        List<Consignment> consignmentList = consignmentRepository.findAllConsignmentByAccountId(account.getId());
        return consignmentList;
    }

    public Set<ConsignmentDetailsResponse> viewConsignmentDetails(Long consignmentId){
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        Set<ConsignmentDetails> consignmentDetailsSet = consignment.getConsignmentDetailsSet();
        if(consignmentDetailsSet.isEmpty()){
            throw new RuntimeException("Not found");
        }
        Set<ConsignmentDetailsResponse> consignmentDetails = consignmentDetailsSet.stream()
                .map(consignmentDetail -> new ConsignmentDetailsResponse(
                        consignmentDetail.getId(),
                        consignmentDetail.getImage(),
                        consignmentDetail.getPrice(),
                        consignmentDetail.getQuantity(),
                        consignmentDetail.getName()))
                .collect(Collectors.toSet());
        return consignmentDetails;
    }

    public void checkAndUpdateConsignmentStatus() {
        List<Consignment> consignments = consignmentRepository.findAll();

        for (Consignment consignment : consignments) {

            if (consignment.getEnd_date().isBefore(LocalDate.now()) && consignment.getStatus() == StatusConsign.VALID) {

                consignment.setStatus(StatusConsign.EXPIRED);
                consignmentRepository.save(consignment);
            } else {
                System.out.println("Still valid");
            }
        }
    }
    @Scheduled(cron = "0 0 0 * * ?") // Chạy hàng ngày lúc nửa đêm
    public void scheduledConsignmentStatusCheck() {
        checkAndUpdateConsignmentStatus();
    }


    @Autowired
    EmailService emailService;

    public void notifyConsignmentExpiry() {
        List<Consignment> consignments = consignmentRepository.findAll();

        for (Consignment consignment : consignments) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), consignment.getEnd_date());
            if (daysLeft <=1 && consignment.getStatus() == StatusConsign.VALID) {
                Account account = consignment.getAccount();

                // Tạo nội dung email
                MailBody mailBody = new MailBody();
                mailBody.setTo(account); // Chỉ truyền email thay vì đối tượng account
                mailBody.setSubject("Care Subscription Expiration Notice");
                mailBody.setExpirationTime(new Date()); // Thiết lập thời gian hết hạn, nếu cần
                // Tạo đường link dẫn tới trang web gia hạn
                String renewalLink = "https://example.com/renewal?consignmentId=" + consignment.getId();

                // Thiết lập các biến context cho email
                Context context = new Context();
                context.setVariable("name", account.getEmail());
                context.setVariable("daysLeft", daysLeft);
                context.setVariable("renewalLink", renewalLink);

                // Gửi email
                emailService.sendExpiryNotification(mailBody, context);
            }
        }
    }
    @Scheduled(cron = "0 31 16 * * ?")
    public void scheduledExpiryNotification() {
        notifyConsignmentExpiry();
    }

}
