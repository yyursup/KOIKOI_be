package com.example.SWP.Repository;

import com.example.SWP.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

    @Query("SELECT c FROM Consignment c where c.account.id = :accountId ")
    List<Consignment> findAllConsignmentByAccountId(@Param("accountId") long accountId);
}
