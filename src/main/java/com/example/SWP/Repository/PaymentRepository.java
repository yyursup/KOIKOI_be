package com.example.SWP.Repository;

import com.example.SWP.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT YEAR(p.paymentDate) as year, MONTH(p.paymentDate) as month, SUM(p.PaymentAmount) as totalRevenue " +
            "FROM Payment p " +
            "GROUP BY YEAR(p.paymentDate), MONTH(p.paymentDate)")
    List<Object[]> findMonthlyRevenue();



}
