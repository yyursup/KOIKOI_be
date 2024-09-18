package com.example.SWP.Service;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account register(Account account){
         try{
            Account newAccount = accountRepository.save(account);
            return newAccount;
         }catch (Exception e){
             if(e.getMessage().contains(account.getUsername())){
                 throw new RuntimeException("Duplicate username");
             }else if(e.getMessage().contains(account.getPhone_number())){
                 throw new RuntimeException("Duplicate phone");
             }else{
                 throw new RuntimeException("Duplicate email");
             }

         }
    }

    public List<Account> getAllAccount(){
       List<Account> accountList = accountRepository.findAll();
       return accountList;
    }
}
