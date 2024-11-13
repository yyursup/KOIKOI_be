package com.example.SWP.Service;

import com.example.SWP.Enums.*;
import com.example.SWP.Repository.*;
import com.example.SWP.entity.*;
import com.example.SWP.model.request.TransactionRequest;
import com.example.SWP.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    ConsignmentRepository consignmentRepository;

    @Autowired
    ConsignmentService consignmentService;

    @Autowired
    EmailService emailService;

    @Autowired
    CancelOrderRepository cancelOrderRepository;

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

        double newBalance = manager.getBalance() + ((koiOrder.getTotalAmount() - koiOrder.getShippingPee()) * 0.1);
        manager.setBalance(newBalance);
        setTransactions.add(transactions2);
        // manager to owner
        Transactions transactions3 = new Transactions();
        Account owner = koiOrder.getOrderDetails().iterator().next().getKoi().getAccount();
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


        List<Consignment> consignments = consignmentRepository.findByAccount(customer);
        for (Consignment consignment : consignments) {
            consignment.setStatus(StatusConsign.VALID);
            consignmentRepository.save(consignment);
        }


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

        CanceledOrder canceledOrder = koiOrder.getCanceledOrder();
        if (canceledOrder != null) {
            canceledOrder.setOrderStatus(OrderStatus.REFUND);
        }

        accountRepository.save(manager);
        accountRepository.save(customer);
        transactionsRepository.save(transactions1);
        orderRepository.save(koiOrder);
        cancelOrderRepository.save(canceledOrder);
    }

    public void BuyKoiFromUser(long orderId) {
        // Lấy thông tin đơn hàng cá koi dựa trên orderId
        KoiOrder koiOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Cannot find order with ID: " + orderId));

        // Lấy tài khoản của người bán (chủ cá koi) và manager
        Account seller = koiOrder.getAccount();
        Account manager = accountRepository.findAccountByRole(Role.MANAGER);

        // Kiểm tra nếu manager có đủ tiền trong TopUpBalance
        if (manager.getBalance() < koiOrder.getTotalAmount()) {
            throw new RuntimeException("Insufficient balance in manager's top-up balance");
        }

        // Khởi tạo một đối tượng Payment cho giao dịch
        Payment payment = new Payment();
        payment.setKoiOrder(koiOrder);
        payment.setPaymentDate(new Date());
        payment.setMethod(PaymentType.BANKING);
        payment.setPaymentAmount(koiOrder.getTotalAmount());

        // Khởi tạo tập hợp giao dịch để quản lý các giao dịch liên quan
        Set<Transactions> transactionsSet = new HashSet<>();

        // Tạo giao dịch trừ tiền từ Manager đến Seller (người bán)
        Transactions purchaseTransaction = new Transactions();
        purchaseTransaction.setFrom(manager);
        purchaseTransaction.setTo(seller);
        purchaseTransaction.setTotalAmount(koiOrder.getTotalAmount());
        purchaseTransaction.setTransactionsDate(new Date());
        purchaseTransaction.setDescription("BUY KOI FISH FROM CUSTOMER");
        purchaseTransaction.setStatus(TransactionsEnum.SUCCESS);
        transactionsSet.add(purchaseTransaction);

        // Cập nhật trạng thái đơn hàng
        koiOrder.setOrderStatus(OrderStatus.CONFIRMED);

        // Cập nhật kho cá koi
        updateKoiStock(koiOrder);

        // Trừ tiền từ TopUpBalance của Manager và cộng vào balance của Seller
        manager.setBalance(manager.getBalance() - koiOrder.getTotalAmount());
        seller.setBalance(seller.getBalance() + koiOrder.getTotalAmount());

        // Liên kết giao dịch với Payment và lưu vào hệ thống
        payment.setTransactions(transactionsSet);

        // Lưu các thực thể vào cơ sở dữ liệu
        accountRepository.save(manager);
        accountRepository.save(seller);
        paymentRepository.save(payment);
        orderRepository.save(koiOrder);  // Lưu trạng thái cập nhật của đơn hàng
    }


    private void updateKoiStock(KoiOrder koiOrder) {
        Set<OrderDetails> orderItems = koiOrder.getOrderDetails();

        for (OrderDetails item : orderItems) {
            Koi koi = item.getKoi();
            int currentStock = koi.getQuantity();
            int orderedQuantity = item.getQuantity();

            if (currentStock >= orderedQuantity) {
                koi.setQuantity(currentStock - orderedQuantity);
                if (koi.getQuantity() <= 0) {
                    koi.setStatus("SOLD OUT");
                } else {
                    koi.setStatus("IN STOCK");
                }
                koiRepository.save(koi); // Cập nhật kho trong cơ sở dữ liệu
            } else {
                throw new RuntimeException("Not enough koi stock available for: " + koi.getName());
            }
        }
    }


    private void updateKoiStockForRefund(KoiOrder koiOrder) {
        Set<OrderDetails> orderItems = koiOrder.getOrderDetails();

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

    public void topUpManagerBalance(TransactionRequest transactionRequest) {
        // Lấy thông tin người dùng đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account manager = accountRepository.findAccountByUsername(username);
        if(manager == null) {
            throw new RuntimeException("Account not found");
        }

        // Kiểm tra role
        if (manager.getRole() != Role.MANAGER) {
            throw new RuntimeException("Only managers can perform a top-up.");
        }

        // Lấy số tiền nạp từ request
        double amount = transactionRequest.getAmount();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Cộng số tiền vào balance
        manager.setBalance(manager.getBalance() + amount);

        // Tạo giao dịch nạp tiền
        Transactions transaction = new Transactions();
        transaction.setFrom(null);
        transaction.setTo(manager);
        transaction.setTotalAmount(amount);
        transaction.setTransactionsDate(new Date());
        transaction.setDescription("Manager Top-Up");
        transaction.setStatus(TransactionsEnum.SUCCESS);

        transactionsRepository.save(transaction);
        accountRepository.save(manager);
    }


    // Phương thức rút tiền
    public void withdrawFunds(TransactionRequest transactionRequest) {
        // Lấy thông tin người dùng đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }


        // Lấy số tiền cần rút từ request
        double amount = transactionRequest.getAmount();

        // Kiểm tra số dư
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance for withdrawal");
        }

        if (account.getRole() == Role.MANAGER) {
            processWithdrawal(account, amount);
            emailService.sendWithdrawalConfirmationEmail(account, amount, transactionRequest);
        } else {
            // Tạo giao dịch pending chờ Manager xác nhận
            Transactions pendingTransaction = new Transactions();
            pendingTransaction.setFrom(account);
            pendingTransaction.setTotalAmount(amount); // Sử dụng số tiền từ request
            pendingTransaction.setTransactionsDate(new Date());
            pendingTransaction.setDescription("Withdrawal request pending Manager approval");
            pendingTransaction.setStatus(TransactionsEnum.PENDING);
            pendingTransaction.setAccountNumber(transactionRequest.getAccountNumber());
            pendingTransaction.setAccountName(transactionRequest.getAccountName());
            pendingTransaction.setBankName(transactionRequest.getBankName());

            transactionsRepository.save(pendingTransaction);
            emailService.sendPendingWithdrawalRequestToManager(pendingTransaction);
        }
    }


    // Phương thức xử lý rút tiền cho Manager và khi User được duyệt
    private void processWithdrawal(Account account, double amount) {
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    // Phương thức xác nhận rút tiền của Manager cho user
    public void approveWithdrawalRequest(long transactionId) {
        Transactions transaction = transactionsRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionsEnum.PENDING) {
            throw new RuntimeException("Transaction is not pending approval");
        }

        Account userAccount = transaction.getFrom();
        double amount = transaction.getTotalAmount();

        if (userAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance for withdrawal");
        }

        // Trừ tiền khỏi tài khoản của user và cập nhật giao dịch
        userAccount.setBalance(userAccount.getBalance() - amount);
        transaction.setStatus(TransactionsEnum.SUCCESS);
        transactionsRepository.save(transaction);
        accountRepository.save(userAccount);

        // Tạo đối tượng TransactionRequest từ thông tin trong transaction
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(transaction.getTotalAmount());
        transactionRequest.setAccountNumber(transaction.getAccountNumber());
        transactionRequest.setAccountName(transaction.getAccountName());
        transactionRequest.setBankName(transaction.getBankName());

        // Gửi email xác nhận rút tiền cho user
        emailService.sendWithdrawalConfirmationEmail(userAccount, amount, transactionRequest);
    }


    public void rejectWithdrawalRequest(long transactionId) {
        Transactions transaction = transactionsRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Kiểm tra xem giao dịch có đang chờ xác nhận không
        if (transaction.getStatus() != TransactionsEnum.PENDING) {
            throw new RuntimeException("Transaction is not pending approval");
        }

        // Cập nhật trạng thái giao dịch thành REJECTED
        transaction.setStatus(TransactionsEnum.REJECTED);
        transactionsRepository.save(transaction);

        // Tạo đối tượng TransactionRequest từ thông tin trong transaction
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(transaction.getTotalAmount());
        transactionRequest.setAccountNumber(transaction.getAccountNumber());
        transactionRequest.setAccountName(transaction.getAccountName());
        transactionRequest.setBankName(transaction.getBankName());

        // Gửi email thông báo từ chối rút tiền
        emailService.sendWithdrawalRejectionEmail(transaction.getFrom(), transaction.getTotalAmount(), transactionRequest);
    }

    public List<Transactions> getTransactionsWithoutPayment() {
        // Lấy thông tin người dùng hiện tại
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findAccountByUsername(username);

        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        // Kiểm tra vai trò của tài khoản
        if (account.getRole() == Role.MANAGER) {
            // Nếu là Manager, trả về tất cả các giao dịch không có Payment
            return transactionsRepository.findAllByPaymentIsNull();
        } else {
            // Nếu không phải Manager, chỉ trả về các giao dịch của tài khoản hiện tại
            return transactionsRepository.findAllByPaymentIsNullAndFrom(account);
        }
    }

}

