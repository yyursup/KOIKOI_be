package com.example.SWP.Service;

import com.example.SWP.Enums.Role;
import com.example.SWP.entity.Cart;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.FirebaseAuth;
import com.example.SWP.model.MailBody;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.model.request.LgGg;
import com.example.SWP.model.request.LoginRequest;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.*;
import com.example.SWP.utils.AccountUtils;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmailService emailService;


    @Autowired
    TokenService tokenService;

    @Autowired
    AccountUtils accountUtils;


    public RegisterResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        account.setRole(Role.CUSTOMER);
        account.setStatus("ACTIVE");
        try {
            String originpassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originpassword));
            Cart cart = new Cart();
            cart.setAccount(account);
            account.setCart(cart);
            account.setCreate_date(new Date());
            Account newAccount = accountRepository.save(account);
            MailBody mailBody = new MailBody();
            mailBody.setTo(newAccount);
            mailBody.setSubject("WELCOME TO MY KOI FISH SHOP");
            mailBody.setLink("http://koifish.store/");
            emailService.sendSimpleMessage2(mailBody);
            RegisterResponse registerResponse = modelMapper.map(newAccount, RegisterResponse.class);
            return registerResponse;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains(account.getUsername())) {
                throw new RuntimeException("Duplicate username");
            } else if (e.getMessage().contains(account.getPhone_number())) {
                throw new RuntimeException("Duplicate phone");
            } else {
                throw new RuntimeException("Duplicate email");
            }

        }
    }

    public LoginResponse loginWithUserName(LoginRequest loginRequest){
       try{
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   loginRequest.getUsername(),
                   loginRequest.getPassword()
           ));
    Account account = (Account) authentication.getPrincipal();
    LoginResponse loginResponse = modelMapper.map(account, LoginResponse.class);
    loginResponse.setToken(tokenService.generateToken(account));
    return loginResponse;
       }catch (Exception e){
           throw new RuntimeException("User or password not found");
       }
    }


    public List<ViewProfileResponse> getAllAccount() {
        List<Account> accountList = accountRepository.findAccountsByIsDeletedFalse();


        return accountList.stream().map(account ->
                modelMapper.map(account, ViewProfileResponse.class)).collect(Collectors.toList());
    }

    public List<ViewProfileResponse> getAllAccount1() {
        List<Account> accountList = accountRepository.findAccountsByRole(Role.CUSTOMER);


        return accountList.stream().map(account ->
                modelMapper.map(account, ViewProfileResponse.class)).collect(Collectors.toList());
    }



    public ViewProfileResponse viewProfile(){
        try{
            Account account = accountUtils.getCurrentAccount();
            return modelMapper.map(account,ViewProfileResponse.class);
        }catch (Exception e){
            throw new RuntimeException("Can not found this account");
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

    public UpdateAndDeleteProfileResponse deleteAccount(Long accountId) {
        Account oldAccount = accountRepository.findAccountById(accountId);

        if (oldAccount == null) {
            throw new RuntimeException("Can not found this id" + accountId);

        }
        oldAccount.setDeleted(true);
        accountRepository.save(oldAccount);
        return modelMapper.map(oldAccount,UpdateAndDeleteProfileResponse.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findAccountByUsername(username);
    }

    public AccountResponse loginGoogle(LgGg token) {
        try{
            FirebaseToken decodeToken = FirebaseAuth.getInstance().verifyIdToken(token.getToken());
            String email = decodeToken.getEmail();
            Account user = accountRepository.findAccountByEmail(email);
            if(user == null) {
                Account newUser = new Account();
                Cart cart = new Cart();
                cart.setAccount(newUser);
                newUser.setCart(cart);
                newUser.setFullName(decodeToken.getName());
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.setRole(Role.CUSTOMER);
                user = accountRepository.save(newUser);
            }

            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setId(user.getId());
            accountResponse.setUsername(user.getUsername());
            accountResponse.setEmail(user.getEmail());
            accountResponse.setRole(user.getRole());
            accountResponse.setToken(tokenService.generateToken(user));
            return accountResponse;
        } catch (FirebaseAuthException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public double getBalance() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm tài khoản liên kết với tên người dùng đó
        Account account = accountRepository.findAccountByUsername(username);

        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        // Trả về số dư hiện tại của tài khoản người dùng
        return account.getBalance();
    }

}
