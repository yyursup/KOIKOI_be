package com.example.SWP.Repository;

import com.example.SWP.Enums.TypeOfConsign;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.Consignment;
import com.example.SWP.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

    @Query("SELECT c FROM Consignment c where c.account.id = :accountId ")
    List<Consignment> findAllConsignmentByAccountId(@Param("accountId") long accountId);

    @Query("SELECT COUNT(c) > 0 FROM Consignment c WHERE c.orderDetails = :orderDetails AND c.status = 'VALID'")
    boolean existsByOrderDetails(@Param("orderDetails") OrderDetails orderDetails);

    List<Consignment> findByAccount(Account account);

    List<Consignment> findAllConsignTypeByAccount(Account account, TypeOfConsign type);
}