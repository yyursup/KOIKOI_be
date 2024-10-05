package com.example.SWP.Repository;

import com.example.SWP.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface VoucherRepository extends JpaRepository<Voucher, Long> {
      Voucher findVoucherById(long id);

      List<Voucher> findVouchersByIsDeletedFalse();

      Optional<Voucher> findByCode(UUID code);
}
