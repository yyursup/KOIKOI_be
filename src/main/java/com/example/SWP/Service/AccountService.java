package com.example.SWP.Service;

import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.model.LoginRequest;
import com.example.SWP.model.LoginResponse;
import com.example.SWP.model.RegisterResponse;
import com.example.SWP.model.RegisterRequest;
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

import java.util.List;

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

    public LoginResponse login(LoginRequest loginRequest){
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



    public List<Account> getAllAccount() {
        List<Account> accountList = accountRepository.findAccountsByIsDeletedFalse();
        return accountList;
    }

    public Account accountUpdate(Account account, long AccountId) {
        Account oldAccount = accountRepository.findAccountById(AccountId);

        if (oldAccount == null) {
            throw new RuntimeException("Can not find this id" + AccountId);
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

    public Account deleteAccount(Long accountId) {
        Account oldAccount = accountRepository.findAccountById(accountId);

        if (oldAccount == null) {
            throw new RuntimeException("Can not found this id" + accountId);

        }
        oldAccount.setDeleted(true);
        return accountRepository.save(oldAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findAccountByUsername(username);
    }
}
