package com.example.SWP.Repository;

import com.example.SWP.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT YEAR(p.paymentDate) as year, MONTH(p.paymentDate) as month, " +
            "SUM(p.PaymentAmount) - COALESCE(SUM(ko.totalAmount), 0) as netRevenue " +
            "FROM Payment p " +
            "LEFT JOIN koiOrder ko ON p.koiOrder.id = ko.id AND ko.orderStatus = 'REFUND' " +
            "GROUP BY YEAR(p.paymentDate), MONTH(p.paymentDate)")
    List<Object[]> findMonthlyNetRevenue();


}
