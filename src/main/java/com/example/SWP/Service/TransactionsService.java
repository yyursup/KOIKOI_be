package com.example.SWP.Service;

import com.example.SWP.Enums.OrderStatus;
import com.example.SWP.Enums.PaymentType;
import com.example.SWP.Enums.Role;
import com.example.SWP.Enums.TransactionsEnum;
import com.example.SWP.Repository.*;
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

    @Autowired
    TransactionsRepository transactionsRepository;

    public void createTransactions(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setMethod(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());
        koiOrder.setConfirmDate(new Date());
        updateKoiStock(koiOrder);

        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        Account customer = koiOrder.getAccount();
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
        koiOrder.setOrderStatus(OrderStatus.CONFIRMED);

        payment.setKoiOrder(koiOrder);

        double newBalance = manager.getBalance() + koiOrder.getTotalAmount();
        manager.setBalance(newBalance);
        setTransactions.add(transactions2);


        payment.setTransactions(setTransactions);

        accountRepository.save(manager);
        paymentRepository.save(payment);

    }
    public void createTransactions3(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setMethod(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());
        koiOrder.setConfirmDate(new Date());
        updateKoiStock(koiOrder);

        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        Account customer = koiOrder.getAccount();
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
        koiOrder.setOrderStatus(OrderStatus.CONFIRMED);
        payment.setKoiOrder(koiOrder);

        double newBalance = manager.getBalance() + ((koiOrder.getTotalAmount() - koiOrder.getShippingPee()) *0.1);
        manager.setBalance(newBalance);
        setTransactions.add(transactions2);
        // manager to owner
        Transactions transactions3 = new Transactions();
        Account owner = koiOrder.getOrderDetails().get(0).getKoi().getAccount();
        transactions3.setFrom(customer);
        transactions3.setTo(manager);
        transactions3.setPayment(payment);
        transactions3.setTotalAmount(newBalance);
        transactions3.setStatus(TransactionsEnum.SUCCESS);
        transactions3.setDescription("MANAGER TO OWNER");
        double newBalance2 = owner.getBalance() + ((koiOrder.getTotalAmount() - koiOrder.getShippingPee()) * 0.9) + koiOrder.getShippingPee();
        owner.setBalance(newBalance2);
        setTransactions.add(transactions3);


        payment.setTransactions(setTransactions);

        accountRepository.save(manager);
        paymentRepository.save(payment);

    }
    public void createTransactionsForConsign(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setMethod(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());
        koiOrder.setConfirmDate(new Date());
        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        Account customer = koiOrder.getAccount();
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
        koiOrder.setOrderStatus(OrderStatus.CONFIRMED);

        payment.setKoiOrder(koiOrder);

        double newBalance = manager.getBalance() + koiOrder.getTotalAmount();
        manager.setBalance(newBalance);
        setTransactions.add(transactions2);

        payment.setTransactions(setTransactions);

        accountRepository.save(manager);
        paymentRepository.save(payment);

    }
    public void createTransactionForExtendConsign(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setMethod(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());
        koiOrder.setConfirmDate(new Date());
        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        Account customer = koiOrder.getAccount();
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
        koiOrder.setOrderStatus(OrderStatus.CONFIRMED);

        payment.setKoiOrder(koiOrder);

        double newBalance = manager.getBalance() + koiOrder.getTotalAmount();
        manager.setBalance(newBalance);
        setTransactions.add(transactions2);


        payment.setTransactions(setTransactions);

        accountRepository.save(manager);
        paymentRepository.save(payment);

    }
    public void createTransactionForCancelConsign(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Set<Transactions> transactions = new HashSet<>();

        Transactions transactions1= new Transactions();
        Account customer = koiOrder.getAccount();
        Account manager = accountRepository.findAccountByRole(Role.MANAGER);
        transactions1.setFrom(manager);
        transactions1.setTo(customer);
        transactions1.setTotalAmount(koiOrder.getTotalAmount());
        transactions1.setTransactionsDate(new Date());
        transactions1.setDescription("REFUND FOR CUSTOMER");
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        koiOrder.setOrderStatus(OrderStatus.REFUND);


        double newBalance =  customer.getBalance() + koiOrder.getTotalAmount() ;
        double newBalance2 = manager.getBalance() - koiOrder.getTotalAmount();
        manager.setBalance(newBalance2);
        customer.setBalance(newBalance);

        transactions.add(transactions1);


        transactionsRepository.save(transactions1);
        accountRepository.save(manager);

    }

    public void createTransactions2(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not found order"));

        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        Account customer = koiOrder.getAccount();
        Account manager = accountRepository.findAccountByRole(Role.MANAGER);

        transactions1.setFrom(manager);
        transactions1.setTo(customer);
        transactions1.setTotalAmount(koiOrder.getTotalAmount());
        transactions1.setTransactionsDate(new Date());
        transactions1.setDescription("REFUND FOR CUSTOMER");
        transactions1.setStatus(TransactionsEnum.REFUND);
        koiOrder.setOrderStatus(OrderStatus.REFUND);

        double newBalance = customer.getBalance() + koiOrder.getTotalAmount();
        double newBalance2 = manager.getBalance() - koiOrder.getTotalAmount();
        manager.setBalance(newBalance2);
        customer.setBalance(newBalance);

        setTransactions.add(transactions1);

        updateKoiStockForRefund(koiOrder);

        accountRepository.save(manager);
        accountRepository.save(customer);
        transactionsRepository.save(transactions1);
        orderRepository.save(koiOrder);
    }

    public void BuyKoiFromUser(long id) {
        KoiOrder koiOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find order with ID: " + id));

        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setMethod(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());
        koiOrder.setConfirmDate(new Date());

        Account owner = koiOrder.getAccount();
        Account manager = accountRepository.findAccountByRole(Role.MANAGER);

        Set<Transactions> setTransactions = new HashSet<>();

        Transactions transactions1 = new Transactions();
        transactions1.setFrom(manager);
        transactions1.setTo(owner);
        transactions1.setTotalAmount(koiOrder.getTotalAmount());
        transactions1.setTransactionsDate(new Date());
        transactions1.setDescription("BUY KOI FISH FROM CUSTOMER");
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        setTransactions.add(transactions1);
        koiOrder.setOrderStatus(OrderStatus.CONFIRMED);
        updateKoiStock(koiOrder);

        // Update balances
        manager.setBalance(manager.getBalance() - koiOrder.getTotalAmount());
        owner.setBalance(owner.getBalance() + koiOrder.getTotalAmount());
        payment.setTransactions(setTransactions);

        // Save entities
        accountRepository.save(manager);
        accountRepository.save(owner);
        paymentRepository.save(payment);
        orderRepository.save(koiOrder);  // Save the updated order status
    }

    private void updateKoiStock(KoiOrder koiOrder) {
        List<OrderDetails> orderItems = koiOrder.getOrderDetails();

        for (OrderDetails item : orderItems) {
            Koi koi = item.getKoi();
            int currentStock = koi.getQuantity();
            int orderedQuantity = item.getQuantity();

            if (currentStock >= orderedQuantity) {
                koi.setQuantity(currentStock - orderedQuantity);
                koiRepository.save(koi);// Update stock in the database
                if(koi.getQuantity() < 0 || koi.getQuantity() == 0){
                   koi.setStatus("SOLD OUT");
                }  else  {
                    koi.setStatus("IN STOCK");
                }

            } else {
                throw new RuntimeException("Not enough koi stock available for: " + koi.getName());
            }
        }
    }

    private void updateKoiStockForRefund(KoiOrder koiOrder) {
        List<OrderDetails> orderItems = koiOrder.getOrderDetails();

        for (OrderDetails item : orderItems) {
            Koi koi = item.getKoi();
            int currentStock = koi.getQuantity();
            int orderedQuantity = item.getQuantity();

            koi.setQuantity(currentStock + orderedQuantity);
            koiRepository.save(koi);
            koi.setStatus("IN STOCK");
            // Update stock in the database
        }
    }

}

