package com.example.SWP.Service;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.Repository.ForgotPasswordRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.ForgotPassword;
import com.example.SWP.model.ChangePassword;
import com.example.SWP.model.MailBody;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
@Service
public class ForgotPasswordService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    EmailService emailService;

    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;



    public ForgotPassword verifyEmail(String email){
        Account account = accountRepository.findAccountByEmail(email);


        if(account == null){

            throw new RuntimeException("Can not found this account");

        }else{

            int otp = otpGenerator();
            MailBody mailBody = MailBody.builder()
                    .to(account)
                    .subject("OTP")
                    .otp(otp)
                    .build();

            ForgotPassword fp = new ForgotPassword(
                    otp,
                    new Date(System.currentTimeMillis() + 5 * 60 * 1000),
                    account
            );

            emailService.sendSimpleMessage(mailBody);
            return forgotPasswordRepository.save(fp);
        }
    }


    public ForgotPassword verifyOTP(Integer otp, String email) {

        Account account = accountRepository.findAccountByEmail(email);


        if (account == null) {
            throw new RuntimeException("Cannot find this account with email: " + email);
        }


        ForgotPassword fp = forgotPasswordRepository.findByOtpAndAccount(otp,account);


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
        Account account = accountRepository.findAccountByEmail(email);

        if(account == null ){
            throw new RuntimeException("Cannot find this account with email: " + email);
        }


        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            throw new RuntimeException("password or repeat password is wrong! ");
        }

        String originPassword = changePassword.password();
        String encodedPassword = passwordEncoder.encode(originPassword);
        account.setPassword(encodedPassword);

        accountRepository.save(account);
        ForgotPassword fp = forgotPasswordRepository.findForgotPasswordByAccount(account);
        forgotPasswordRepository.deleteById(fp.getFpid());
        return changePassword;
    }

    public Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
