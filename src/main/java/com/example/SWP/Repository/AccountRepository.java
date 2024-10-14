package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountById(long id);

    List<Account> findAccountsByIsDeletedFalse();

    Account findAccountByUsername(String username);

    Account findAccountByEmail(String email);

}
