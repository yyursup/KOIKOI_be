package com.example.SWP.utils;

import com.example.SWP.entity.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountUtils {
    public Account getCurrentAccount() {
      return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
