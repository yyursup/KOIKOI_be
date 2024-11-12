package com.example.SWP.Service;

import com.example.SWP.Enums.Role;
import com.example.SWP.Repository.AccountRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.model.*;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.RegisterResponse;
import com.example.SWP.model.response.UpdateAndDeleteProfileResponse;
import com.example.SWP.model.response.ViewProfileResponse;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    ModelMapper modelMapper;
    public RegisterResponse registerForStaff(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        account.setRole(Role.STAFF);
        try {
            String originpassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originpassword));
            account.setCreate_date(new Date());
            Account newAccount = accountRepository.save(account);
            MailBody mailBody = new MailBody();
            mailBody.setTo(newAccount);
            mailBody.setSubject("WELCOME TO MY KOI FISH SHOP");
            mailBody.setLink("http://koifish.store/");
            emailService.sendSimpleMessage2(mailBody);
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

    public List<ViewProfileResponse> getAllAccount() {
        List<Account> accountList = accountRepository.findAccountsByRole(Role.STAFF);


        return accountList.stream().map(account ->
                modelMapper.map(account, ViewProfileResponse.class)).collect(Collectors.toList());
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

    public ViewProfileResponse viewProfile(){
        try{
            Account account = accountUtils.getCurrentAccount();
            return modelMapper.map(account,ViewProfileResponse.class);
        }catch (Exception e){
            throw new RuntimeException("Can not found this account");
        }

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
}
