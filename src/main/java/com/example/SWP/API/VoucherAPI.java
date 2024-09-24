package com.example.SWP.API;

import com.example.SWP.Service.VoucherService;
import com.example.SWP.entity.Voucher;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/voucher")
@RestController
@CrossOrigin("*")
public class VoucherAPI {

    @Autowired
    VoucherService voucherService;

    @PostMapping("create")
    public ResponseEntity createVoucher(@Valid @RequestBody Voucher voucher){
        Voucher newVoucher = voucherService.createVoucher(voucher);
        return ResponseEntity.ok(newVoucher);
    }

    @GetMapping("get")
    public ResponseEntity getAllVoucher(){
        List<Voucher> voucherList = voucherService.getAllVoucher();
        return ResponseEntity.ok(voucherList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateVoucher(@Valid @RequestBody Voucher voucher, @PathVariable long id){
        try{
            Voucher newVoucher = voucherService.updateVoucher(voucher,id);
            return ResponseEntity.ok(newVoucher);
        }catch (Exception e){
            throw new RuntimeException("The voucher id :" + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteVoucher(@PathVariable long id){
        try{
            Voucher oldVoucher = voucherService.deleteVoucher(id);
            return ResponseEntity.ok(oldVoucher);
        }catch (Exception e){
            throw new RuntimeException("The voucher id :" + id + " not found ");
        }
    }
}
