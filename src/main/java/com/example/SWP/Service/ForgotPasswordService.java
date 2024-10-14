package com.example.SWP.Service;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.model.ForgotPasswordRequest;
import com.example.SWP.model.MailBody;
import com.example.SWP.model.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    EmailService emailService;

    @Autowired
    TokenService tokenService;



//    public ForgotPassword verifyEmail(String email){
//        Account account = accountRepository.findAccountByEmail(email);
//
//
//        if(account == null){
//
//            throw new RuntimeException("Can not found this account");
//
//        }else{
//
//            int otp = otpGenerator();
//            MailBody mailBody = MailBody.builder()
//                    .to(account)
//                    .subject("OTP")
//                    .otp(otp)
//                    .build();
//
//            ForgotPassword fp = new ForgotPassword(
//                    otp,
//                    new Date(System.currentTimeMillis() + 5 * 60 * 1000),
//                    account
//            );
//
//            emailService.sendSimpleMessage(mailBody);
//            return forgotPasswordRepository.save(fp);
//        }
//    }
//
//
//    public ForgotPassword verifyOTP(Integer otp, String email) {
//
//        Account account = accountRepository.findAccountByEmail(email);
//
//
//        if (account == null) {
//            throw new RuntimeException("Cannot find this account with email: " + email);
//        }
//
//
//        ForgotPassword fp = forgotPasswordRepository.findByOtpAndAccount(otp,account);
//
//
//        if (fp == null) {
//            throw new RuntimeException("Invalid OTP for email: " + email);
//        }
//
//
//        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
//
//            forgotPasswordRepository.deleteById(fp.getFpid());
//            throw new RuntimeException("OTP has expired for email: " + email);
//        }
//
//
//        return forgotPasswordRepository.save(fp);
//    }
//
//    public ChangePassword changePassword(ChangePassword changePassword, String email){
//        Account account = accountRepository.findAccountByEmail(email);
//
//        if(account == null ){
//            throw new RuntimeException("Cannot find this account with email: " + email);
//        }
//
//
//        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
//            throw new RuntimeException("password or repeat password is wrong! ");
//        }
//
//        String originPassword = changePassword.password();
//        String encodedPassword = passwordEncoder.encode(originPassword);
//        account.setPassword(encodedPassword);
//
//        accountRepository.save(account);
//        ForgotPassword fp = forgotPasswordRepository.findForgotPasswordByAccount(account);
//        forgotPasswordRepository.deleteById(fp.getFpid());
//        return changePassword;
//    }
//
//    public Integer otpGenerator(){
//        Random random = new Random();
//        return random.nextInt(100_000,999_999);
//    }
public Account getCurrentAccount() {
    Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return accountRepository.findAccountById(account.getId());
}


public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
    Account  account = accountRepository.findAccountByEmail(forgotPasswordRequest.getEmail());
    if(account == null) {
        throw new RuntimeException("Account Not Found");
    } else {
        MailBody mailBody  = new MailBody();
        mailBody.setTo(account);
        mailBody.setSubject("Reset Password");
        mailBody.setLink("https://chatgpt.com/c/6707ec1a-b6f0-800d-b47b-1bb34e4a9d7d/?token="+ tokenService.generateToken(account));

        emailService.sendSimpleMessage(mailBody);
    }
}

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        accountRepository.save(account);
    }
}
