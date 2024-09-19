package com.example.SWP.Service;

import com.example.SWP.Repository.VoucherRepository;
import com.example.SWP.entity.Voucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    public Voucher createVoucher(Voucher voucher){
        try{
            Voucher newVoucher = voucherRepository.save(voucher);
            return newVoucher;
        }catch (Exception e){
            throw new RuntimeException("Duplicate code");
        }
    }

    public List<Voucher> getAllVoucher(){
        List<Voucher> vouchers = voucherRepository.findVouchersByIsDeletedFalse();
        return vouchers;
    }

    public Voucher updateVoucher(Voucher voucher, long voucherId){
        Voucher oldVoucher = voucherRepository.findVoucherById(voucherId);

        if(oldVoucher == null){
            throw new RuntimeException("can not found this id of voucher");
        }
        oldVoucher.setCode(voucher.getCode());
        oldVoucher.setDescription(voucher.getDescription());
        oldVoucher.setDiscount_amount(voucher.getDiscount_amount());
        oldVoucher.setStart_date(voucher.getStart_date());
        oldVoucher.setEnd_date(voucher.getEnd_date());
        oldVoucher.setIs_active(voucher.getIs_active());
        return voucherRepository.save(oldVoucher);
    }

    public Voucher deleteVoucher(long voucherId){
        Voucher oldVoucher = voucherRepository.findVoucherById(voucherId);

        if(oldVoucher == null){
            throw new RuntimeException("can not found");
        }
        oldVoucher.setDeleted(true);
        return voucherRepository.save(oldVoucher);
    }


}
