package com.example.SWP.Repository;

import com.example.SWP.entity.SystemProfit;
import com.example.SWP.entity.Transactions;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SystemProfitRepository extends JpaRepository<SystemProfit, Long> {

    Optional<SystemProfit> findByTransactions(Transactions transactions);

    @Query("SELECT SUM(sp.balance) from SystemProfit sp")
    Double getTotalSystemProfit();
}
