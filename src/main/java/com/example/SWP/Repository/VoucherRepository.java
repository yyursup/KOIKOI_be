package com.example.SWP.Repository;

import com.example.SWP.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface VoucherRepository extends JpaRepository<Voucher, Long> {
      Voucher findVoucherById(long id);

      List<Voucher> findVouchersByIsDeletedFalse();
}
