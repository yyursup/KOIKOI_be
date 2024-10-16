package com.example.SWP.Service;

import com.example.SWP.Enums.Role;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.SystemProfitRepository;
import com.example.SWP.Repository.TransactionsRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.SystemProfit;
import com.example.SWP.entity.Transactions;
import com.example.SWP.model.*;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.RegisterResponse;
import com.example.SWP.model.response.UpdateAndDeleteProfileResponse;
import com.example.SWP.model.response.ViewProfileResponse;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public RegisterResponse registerForManager(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        account.setRole(Role.MANAGER);
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
        oldAccount.setState(updateProfileRequest.getState());
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

    public double systemProfit(int month, int year) {
        List<Transactions> transactions = transactionsRepository.findAll();

        double totalProfit = 0;

        for (Transactions transaction : transactions) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transaction.getTransactionsDate());

            int transactionMonth = calendar.get(Calendar.MONTH) + 1;
            int transactionYear = calendar.get(Calendar.YEAR);

            if (transactionMonth == month && transactionYear == year) {
                Account customer = transaction.getFrom();
                Account manager = transaction.getTo();

                if (customer != null && manager != null && manager.getRole() == Role.MANAGER
                        && "CUSTOMER TO MANAGER".equals(transaction.getDescription())) {

                    // Kiểm tra xem transaction đã tồn tại trong SystemProfit hay chưa
                    Optional<SystemProfit> existingProfit = systemProfitRepository.findByTransactions(transaction);

                    if (existingProfit.isPresent()) {
                        // Nếu đã tồn tại, cập nhật profit
                        SystemProfit systemProfit = existingProfit.get();
                        systemProfitRepository.save(systemProfit);
                    } else {
                        // Nếu chưa tồn tại, thêm mới
                        SystemProfit systemProfit = new SystemProfit();
                        totalProfit = transaction.getTotalAmount();
                        systemProfit.setBalance(totalProfit);
                        systemProfit.setDescription("Profit for " + month + " / " + year
                                + " from transaction ID " + transaction.getId());
                        systemProfit.setDate(new Date());
                        systemProfit.setTransactions(transaction);
                        systemProfitRepository.save(systemProfit);
                    }
                }
            }
        }
        return totalProfit;
    }

    public Double getTotalSystemProfit(){
        Double totalProfit = systemProfitRepository.getTotalSystemProfit();
        if(totalProfit == null){
            return 0.0;
        }
        return totalProfit;
    }
}

