package com.example.SWP.Repository;

import com.example.SWP.Enums.Type;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.Enums.OrderStatus;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<KoiOrder, Long> {

    @Query("SELECT o FROM KoiOrder o where o.account.id = :accountId ")
    List<KoiOrder> findAllOrdersByAccountId(@Param("accountId") long accountId);

    List<KoiOrder> findByOrderStatusAndType(OrderStatus orderStatus, Type type);

    KoiOrder findKoiOrderById(long id);

    List<KoiOrder> findAllByOrderStatusAndProcessingDateLessThan(OrderStatus orderStatus, Date processingDate);

    List<KoiOrder> findKoiOrderByType(Type type);


}
