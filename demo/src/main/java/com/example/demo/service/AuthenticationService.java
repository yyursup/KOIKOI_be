package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.Service.EmailService;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.EmailDetail;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.ArrayList;
import java.util.List;

@Service //đánh dấu cho thằng spring boot biết đây là lớp service
public class AuthenticationService implements UserDetailsService {

    //xử lí logic, xử lí nghiệp vụ...
    List<Account> accounts = new ArrayList<>();
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    private EmailService emailService;


    public AccountResponse register(RegisterRequest registerRequest) {

        try {
            Account account = modelMapper.map(registerRequest, Account.class);
            String originPassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            Account newAccount = accountRepository.save(account);
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(newAccount);
            emailDetail.setSubject("WELCOME TO MY KOI FISH SHOP");
            emailDetail.setLink("http://koifish.store/");
            emailService.sendEmail(emailDetail);

            return modelMapper.map(newAccount, AccountResponse.class);
        } catch (ConstraintViolationException e) {
            if (e.getMessage().contains(registerRequest.getUsername())) {
                throw new DuplicateEntity("Dupliacte userName");
            } else {
                // throw new DuplicateEntity("Dupliacte phone");
                throw new DuplicateEntity(e.getMessage());
//             return null;
            }
        }

    }


    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));

            //=> tài khoản có tồn tại
            Account account = (Account) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse;
        }catch (Exception e) {
            throw new EntityNotFoundException("Username or Password invalid!!!");
        }
    }

    @GetMapping("account")
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return accountRepository.findAccountByPhone(phone);
    }

    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }
}
