package com.example.SWP.Repository;

import com.example.SWP.Enums.ApprovalStatus;
import com.example.SWP.Enums.StatusConsign;
import com.example.SWP.Enums.TypeOfConsign;
import com.example.SWP.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

    @Query("SELECT c FROM Consignment c where c.account.id = :accountId ")
    List<Consignment> findAllConsignmentByAccountId(@Param("accountId") long accountId);

    List<Consignment> findByAccount(Account account);

    List<Consignment> findByAccountAndType(Account account, TypeOfConsign type);

    List<Consignment> findByType(TypeOfConsign type);

    List<Consignment> findByApprovalStatus(ApprovalStatus approvalStatus);

    List<Consignment> findAllByAccountAndType(Account account, TypeOfConsign type);

    List<Consignment> findByKoiOrder(KoiOrder koiOrder);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Consignment c " +
            "JOIN c.consignmentDetailsSet cd " +
            "WHERE cd.koi = :koi AND c.status = :status")
    boolean existsByKoiAndStatus(@Param("koi") Koi koi, @Param("status") StatusConsign status);

}
