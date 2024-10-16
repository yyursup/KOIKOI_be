package com.example.SWP.Service;

import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.PaymentType;
import com.example.SWP.Enums.Role;
import com.example.SWP.Enums.TransactionsEnum;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.KoiRepository;
import com.example.SWP.Repository.OrderRepository;
import com.example.SWP.Repository.PaymentRepository;
import com.example.SWP.entity.*;
import com.example.SWP.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransactionsService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    KoiRepository koiRepository;

    public void createTransactions(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setPaymentType(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());
        koiOrder.setConfirmDate(new Date());

        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        Account customer = accountUtils.getCurrentAccount();
        //VNPAY TO CUSTOMER
        transactions1.setFrom(null);
        transactions1.setTo(customer);
        transactions1.setPayment(payment);
        transactions1.setTotalAmount(koiOrder.getTotalAmount());
        transactions1.setTransactionsDate(new Date());
        transactions1.setDescription("VNPAY TO CUSTOMER");
        transactions1.setStatus(TransactionsEnum.SUCCESS);

        setTransactions.add(transactions1);

        // customer to manager
        Transactions transactions2 = new Transactions();
        Account manager = accountRepository.findAccountByRole(Role.MANAGER);
        transactions2.setFrom(customer);
        transactions2.setTo(manager);
        transactions2.setPayment(payment);
        transactions2.setTotalAmount(koiOrder.getTotalAmount());
        transactions2.setTransactionsDate(new Date());
        transactions2.setDescription("CUSTOMER TO MANAGER");
        transactions2.setStatus(TransactionsEnum.SUCCESS);
        koiOrder.setOrderStatus(OrderStatus.PAID);
        updateKoiStock(koiOrder);


        payment.setKoiOrder(koiOrder);

        double newBalance = manager.getBalance() + koiOrder.getTotalAmount();
        manager.setBalance(newBalance);
        setTransactions.add(transactions2);


        payment.setTransactions(setTransactions);

        accountRepository.save(manager);
        paymentRepository.save(payment);

    }

    private void updateKoiStock(KoiOrder koiOrder) {
        // Giả định koiOrder có danh sách các item hoặc koi đã đặt hàng
        Set<OrderDetails> orderItems = koiOrder.getOrderDetails();

        for (OrderDetails item : orderItems) {
            Koi koi = item.getKoi(); // Giả sử KoiOrderItem có quan hệ với Koi

            // Giảm số lượng cá koi theo số lượng đã đặt trong đơn hàng
            int currentStock = koi.getQuantity();
            int orderedQuantity = item.getQuantity();

            if (currentStock >= orderedQuantity) {
                koi.setQuantity(currentStock - orderedQuantity); // Giảm số lượng trong kho
            } else {
                throw new RuntimeException("Not enough koi stock available for: " + koi.getName());
            }

            // Lưu lại trạng thái cập nhật số lượng
            koiRepository.save(koi);
        }
    }
}
