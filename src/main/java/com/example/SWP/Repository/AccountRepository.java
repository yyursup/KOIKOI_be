package com.example.SWP.Repository;

import com.example.SWP.Enums.Role;
import com.example.SWP.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findAccountById(long id);

    List<Account> findAccountsByIsDeletedFalse();

    Account findAccountByUsername(String username);

    Account findAccountByEmail(String email);

    Account findAccountByRole(Role role);

    List<Account> findAccountsByRole(Role role);


    @Query("select count(a) from Account a where a.role = :role")
    long countByRole(@Param("role") Role role);

}
