package com.example.SWP.Service;

import com.example.SWP.Enums.Role;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.PaymentRepository;
import com.example.SWP.Repository.SystemProfitRepository;
import com.example.SWP.Repository.TransactionsRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.Payment;
import com.example.SWP.entity.SystemProfit;
import com.example.SWP.entity.Transactions;
import com.example.SWP.model.*;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.*;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManagerService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    SystemProfitRepository systemProfitRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PaymentRepository paymentRepository;

    public RegisterResponse registerForManager(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        account.setRole(Role.MANAGER);
        account.setStatus("ACTIVE");
        try {
            String originpassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originpassword));
            account.setCreate_date(new Date());
            Account newAccount = accountRepository.save(account);
            MailBody mailBody = new MailBody();
            mailBody.setTo(newAccount);
            mailBody.setSubject("WELCOME TO MY KOI FISH SHOP");
            mailBody.setLink("http://koifish.store/");
            emailService.sendSimpleMessage2(mailBody);
            return modelMapper.map(newAccount, RegisterResponse.class);
        } catch (Exception e) {
            if (e.getMessage().contains(account.getUsername())) {
                throw new RuntimeException("Duplicate username");
            } else if (e.getMessage().contains(account.getPhone_number())) {
                throw new RuntimeException("Duplicate phone");
            } else {
                throw new RuntimeException("Duplicate email");
            }

        }
    }

    public UpdateAndDeleteProfileResponse accountUpdate(UpdateProfileRequest updateProfileRequest, Long AccountId) {
        Account oldAccount = accountRepository.findAccountById(AccountId);

        if (oldAccount == null) {
            throw new RuntimeException("Can not find this id" + AccountId);
        }
        oldAccount.setUsername(updateProfileRequest.getUsername());
        oldAccount.setFullName(updateProfileRequest.getFullname());
        oldAccount.setPhone_number(updateProfileRequest.getPhone_number());
        oldAccount.setEmail(updateProfileRequest.getEmail());
        oldAccount.setCity(updateProfileRequest.getCity());
        oldAccount.setCountry(updateProfileRequest.getCountry());
        oldAccount.setSpecific_address(updateProfileRequest.getSpecific_Address());


        accountRepository.save(oldAccount);
        return modelMapper.map(oldAccount, UpdateAndDeleteProfileResponse.class);
    }

    public ViewProfileResponse viewProfile() {
        try {
            Account account = accountUtils.getCurrentAccount();
            return modelMapper.map(account, ViewProfileResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Can not found this account");
        }

    }

    public List<PaymentResponse> paymentsIncome(int month, int year) {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentResponse> validPayments = new ArrayList<>();
        double totalIncome = 0;

        for (Payment payment : payments) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(payment.getPaymentDate());

            int paymentMonth = calendar.get(Calendar.MONTH) + 1;
            int paymentYear = calendar.get(Calendar.YEAR);

            if (paymentMonth == month && paymentYear == year) {

                totalIncome += payment.getPaymentAmount();


                Optional<SystemProfit> existingProfit = systemProfitRepository.findByPayment(payment);

                if (existingProfit.isPresent()) {
                    SystemProfit systemProfit = existingProfit.get();
                    systemProfitRepository.save(systemProfit);
                } else {
                    SystemProfit systemProfit = new SystemProfit();
                    systemProfit.setBalance(payment.getPaymentAmount());
                    systemProfit.setDescription("Income for " + month + " / " + year
                            + " from payment ID " + payment.getId());
                    systemProfit.setDate(new Date());
                    systemProfit.setPayment(payment);
                    systemProfitRepository.save(systemProfit);
                }

                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setId(payment.getId());
                paymentResponse.setTotalAmount(payment.getPaymentAmount());
                paymentResponse.setPaymentDate(payment.getPaymentDate());
                paymentResponse.setMethod(payment.getMethod());

                validPayments.add(paymentResponse);
            }
        }

        return validPayments;
    }

    public Double getTotalIncome(){
        Double totalProfit = systemProfitRepository.getTotalSystemProfit();
        if(totalProfit == null){
            return 0.0;
        }
        return totalProfit;
    }
}

