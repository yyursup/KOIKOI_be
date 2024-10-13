package com.example.SWP.Repository;

import com.example.SWP.entity.KoiOrder;
import com.example.SWP.Enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<KoiOrder, Long> {

    List<KoiOrder> findAccountById(long id);

    List<KoiOrder> findByOrderStatus(OrderStatus orderStatus);
}
