package com.example.SWP.Service;

import com.example.SWP.DTO.MailBody;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.ForgotPasswordRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.ForgotPassword;
import com.example.SWP.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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
    ForgotPasswordRepository forgotPasswordRepository;

    public RegisterResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        try {
            String originpassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originpassword));
            Account newAccount = accountRepository.save(account);
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

    public LoginResponse loginWithUserName(LoginRequest loginRequest){
       try{
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   loginRequest.getUsername(),
                   loginRequest.getPassword()
           ));
    Account account = (Account) authentication.getPrincipal();
    return modelMapper.map(account,LoginResponse.class);
       }catch (Exception e){
           throw new RuntimeException("User or password not found");
       }
    }



//    public List<Account> getAllAccount() {
//        List<Account> accountList = accountRepository.findAccountsByIsDeletedFalse();
//        return accountList;
//    }
    public List<ViewProfileResponse> getAllAccount() {
        List<Account> accountList = accountRepository.findAccountsByIsDeletedFalse();


        return accountList.stream().map(account ->
                modelMapper.map(account, ViewProfileResponse.class)).collect(Collectors.toList());
    }

//    public List<ViewProfileResponse> getAllAccount1() {
//        List<Account> accountList = accountRepository.findAccountsByIsDeletedFalse();
//        List<ViewProfileResponse> responses = new ArrayList<>();
//        for(Account account : accountList){
//            responses.add(modelMapper.map(account,ViewProfileResponse.class));
//        }
//        return responses;
//
//
//    }

    public ViewProfileResponse viewProfile(long Accountid){

        try{
            Account account = accountRepository.findAccountById(Accountid);
            return modelMapper.map(account,ViewProfileResponse.class);
        }catch (Exception e){
            throw new RuntimeException("Can not found this account");
        }

    }



    public UpdateAndDeleteProfileResponse accountUpdate(UpdateProfileRequest updateProfileRequest, long AccountId) {
        Account oldAccount = accountRepository.findAccountById(AccountId);

        if (oldAccount == null) {
            throw new RuntimeException("Can not find this id" + AccountId);
        }
        oldAccount.setUsername(updateProfileRequest.getUsername());
        oldAccount.setFullname(updateProfileRequest.getFullname());
        oldAccount.setPhone_number(updateProfileRequest.getPhone_number());
        oldAccount.setEmail(updateProfileRequest.getEmail());
//        oldAccount.setPassword(updateProfileRequest.getPassword());
        oldAccount.setCity(updateProfileRequest.getCity());
        oldAccount.setState(updateProfileRequest.getState());
        oldAccount.setCountry(updateProfileRequest.getCountry());
        oldAccount.setSpecific_Address(updateProfileRequest.getSpecific_Address());
//        oldAccount.setDeleted(account.isDeleted());

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

    public ForgotPassword verifyEmail(String email){
        Account account = accountRepository.findAccountByEmail(email);


        if(account == null){

            throw new RuntimeException("Can not found this account");

        }else{

            int otp = otpGenerator();
            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("This is the OTP for your Forgot password request :"+ otp)
                    .subject("OTP for forgot password request")
                    .build();

            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                    .account(account)
                    .build();

            emailService.sendSimpleMessage(mailBody);
         return forgotPasswordRepository.save(fp);
        }
    }


    public ForgotPassword verifyOTP(Integer otp, String email) {

        Account account = accountRepository.findAccountByEmail(email);


        if (account == null) {
            throw new RuntimeException("Cannot find this account with email: " + email);
        }


        ForgotPassword fp = forgotPasswordRepository.findByOtpAndAccount(otp, account);


        if (fp == null) {
            throw new RuntimeException("Invalid OTP for email: " + email);
        }


        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {

            forgotPasswordRepository.deleteById(fp.getFpid());
            throw new RuntimeException("OTP has expired for email: " + email);
        }


        return forgotPasswordRepository.save(fp);
    }

    public ChangePassword changePassword(ChangePassword changePassword, String email){
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            throw new RuntimeException("password or repeat password is wrong! ");
        }
        String originPassword = changePassword.password();
        String encodedPassword = passwordEncoder.encode(originPassword);
        accountRepository.updatePassword(email,encodedPassword);
        return changePassword;
    }

    public Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }


}
