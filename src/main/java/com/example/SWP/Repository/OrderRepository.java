package com.example.SWP.Repository;

import com.example.SWP.entity.KoiOrder;
import com.example.SWP.Enums.OrderStatus;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<KoiOrder, Long> {

    List<KoiOrder> findAccountById(long id);

    List<KoiOrder> findByOrderStatus(OrderStatus orderStatus);

    KoiOrder findKoiOrderById(long id);

    List<KoiOrder> findAllByOrderStatusAndProcessingDateLessThan(OrderStatus orderStatus, Date processingDate);

}
