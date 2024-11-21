package com.example.SWP.Repository;

import com.example.SWP.Enums.Role;
import com.example.SWP.entity.KoiOrder;
import com.example.SWP.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByKoiOrderId(long id);

    List<OrderDetails> findByKoiOrder(KoiOrder koiOrder);

}
