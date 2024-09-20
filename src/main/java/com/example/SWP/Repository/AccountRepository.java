package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import com.example.SWP.model.GetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountById(long id);

    List<Account> findAccountsByIsDeletedFalse();


    Account findAccountByUsername(String username);
}
