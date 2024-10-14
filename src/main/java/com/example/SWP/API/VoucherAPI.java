package com.example.SWP.API;

import com.example.SWP.Service.VoucherService;
import com.example.SWP.model.Request.VoucherRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/voucher")
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class VoucherAPI {

    @Autowired
    VoucherService voucherService;

    @PostMapping()
    public ResponseEntity createVoucher(@Valid @RequestBody VoucherRequest voucherRequest){
        VoucherRequest newVoucher = voucherService.createVoucher(voucherRequest);
        return ResponseEntity.ok(newVoucher);
    }

    @GetMapping()
    public ResponseEntity getAllVoucher(){
        List<VoucherRequest> voucherList = voucherService.getAllVoucher();
        return ResponseEntity.ok(voucherList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateVoucher(@Valid @RequestBody VoucherRequest voucherRequest, @PathVariable Long id){
        try{
            VoucherRequest newVoucher = voucherService.updateVoucher(voucherRequest,id);
            return ResponseEntity.ok(newVoucher);
        }catch (Exception e){
            throw new RuntimeException("The voucher id :" + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteVoucher(@PathVariable Long id){
        try{
            VoucherRequest oldVoucher = voucherService.deleteVoucher(id);
            return ResponseEntity.ok(oldVoucher);
        }catch (Exception e){
            throw new RuntimeException("The voucher id :" + id + " not found ");
        }
    }
}
