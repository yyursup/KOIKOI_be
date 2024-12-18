package com.example.SWP.Service;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.model.request.ForgotPasswordRequest;
import com.example.SWP.model.MailBody;
import com.example.SWP.model.request.ResetPasswordRequest;
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


    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }


    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Account account = accountRepository.findAccountByEmail(forgotPasswordRequest.getEmail());
        if (account == null) {
            throw new RuntimeException("Account Not Found");
        } else {
            MailBody mailBody = new MailBody();
            mailBody.setTo(account);
            mailBody.setSubject("Reset Password");
            // Thiết lập link với token
            String resetLink = "https://chatgpt.com/c/6707ec1a-b6f0-800d-b47b-1bb34e4a9d7d/?token=" + tokenService.generateToken(account);
            mailBody.setLink(resetLink);

            emailService.sendSimpleMessage(mailBody);
        }
    }


    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        accountRepository.save(account);
    }
}