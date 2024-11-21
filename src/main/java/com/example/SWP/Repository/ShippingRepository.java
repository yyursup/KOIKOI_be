package com.example.SWP.Repository;

import com.example.SWP.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
  List<Shipping> findShippingByCodeShipping(String codeShipping);

  List<Shipping> findByKoiOrderAccountId(Long accountId);
}
