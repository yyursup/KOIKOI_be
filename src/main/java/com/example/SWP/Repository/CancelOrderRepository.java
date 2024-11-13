package com.example.SWP.Repository;

import com.example.SWP.entity.CanceledOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelOrderRepository extends JpaRepository<CanceledOrder, Long> {


}
