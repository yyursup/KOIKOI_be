package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountById(long id);

    List<Account> findAccountsByIsDeletedFalse();



    Account findAccountByUsername(String username);


}
