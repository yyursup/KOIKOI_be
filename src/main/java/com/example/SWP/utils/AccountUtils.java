package com.example.SWP.utils;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountUtils {
    @Autowired
    AccountRepository accountRepository;
    public Account getCurrentAccount() {
    Account account= (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return accountRepository.findAccountById(account.getId());
    }
}
