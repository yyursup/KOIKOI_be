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
       List<Account> accountList = accountRepository.findAccountsByIsDeletedFalse();
       return accountList;
    }

    public Account accountUpdate(Account account, long AccountId){
        Account oldAccount = accountRepository.findAccountById(AccountId);

        if(oldAccount == null){
            throw new RuntimeException("Can not find this id" + AccountId );
        }
        oldAccount.setUsername(account.getUsername());
        oldAccount.setFullname(account.getFullname());
        oldAccount.setPhone_number(account.getPhone_number());
        oldAccount.setEmail(account.getEmail());
        oldAccount.setPassword(account.getPassword());
        oldAccount.setCity(account.getCity());
        oldAccount.setState(account.getState());
        oldAccount.setCountry(account.getCountry());
        oldAccount.setSpecific_Address(account.getSpecific_Address());
        oldAccount.setDeleted(account.isDeleted());
        return accountRepository.save(oldAccount);
    }

    public Account deleteAccount(Long accountId){
        Account oldAccount = accountRepository.findAccountById(accountId);

        if(oldAccount == null){
            throw new RuntimeException("Can not found this id" + accountId);

        }
        oldAccount.setDeleted(true);
        return accountRepository.save(oldAccount);
    }
}
