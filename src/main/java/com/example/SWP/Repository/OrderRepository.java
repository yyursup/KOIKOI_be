package com.example.SWP.Repository;
import com.example.SWP.entity.KoiOrder;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<KoiOrder,Long >  {


}
