package com.example.SWP.Service;

import com.example.SWP.Repository.VoucherRepository;
import com.example.SWP.entity.Voucher;
import com.example.SWP.model.Request.VoucherRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    ModelMapper modelMapper;



    public VoucherRequest createVoucher(VoucherRequest voucherRequest){
            Voucher voucher = modelMapper.map(voucherRequest,Voucher.class);
        try{
            Voucher newVoucher = voucherRepository.save(voucher);
            return modelMapper.map(newVoucher, VoucherRequest.class);
        }catch (Exception e){
            throw new RuntimeException("Duplicate code");
        }
    }

    public List<VoucherRequest> getAllVoucher(){
        List<Voucher> vouchers = voucherRepository.findVouchersByIsDeletedFalse();
        return vouchers.stream().map(voucher ->
                modelMapper.map(voucher, VoucherRequest.class)).
                collect(Collectors.toList());
    }

    public VoucherRequest updateVoucher(VoucherRequest voucherRequest, Long voucherId){
        Voucher oldVoucher = voucherRepository.findVoucherById(voucherId);

        if(oldVoucher == null){
            throw new RuntimeException("can not found this id of voucher");
        }

        oldVoucher.setDescription(voucherRequest.getDescription());
        oldVoucher.setDiscount_amount(voucherRequest.getDiscount_amount());
        oldVoucher.setStart_date(voucherRequest.getStart_date());
        oldVoucher.setEnd_date(voucherRequest.getEnd_date());
        oldVoucher.setIs_active(voucherRequest.getIs_active());
        voucherRepository.save(oldVoucher);
        return modelMapper.map(oldVoucher,VoucherRequest.class);
    }

    public VoucherRequest deleteVoucher(Long voucherId){
        Voucher oldVoucher = voucherRepository.findVoucherById(voucherId);

        if(oldVoucher == null){
            throw new RuntimeException("can not found");
        }
        oldVoucher.setDeleted(true);
         voucherRepository.save(oldVoucher);
         return modelMapper.map(oldVoucher,VoucherRequest.class);
    }


}
