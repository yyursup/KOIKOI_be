package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import com.example.SWP.model.ChangePassword;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountById(long id);

    List<Account> findAccountsByIsDeletedFalse();

    Account findAccountByUsername(String username);

    Account findAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Account a set a.password = ?2 where a.email = ?1")
    void updatePassword(String email, String password);
}
