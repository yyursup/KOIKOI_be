package com.example.SWP.Repository;

import com.example.SWP.Enums.TransactionsEnum;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findAllByPaymentIsNull();

    List<Transactions> findAllByPaymentIsNullAndFrom(Account account);

}
