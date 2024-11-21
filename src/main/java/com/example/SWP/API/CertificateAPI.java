package com.example.SWP.API;

import com.example.SWP.Service.CertificateService;
import com.example.SWP.entity.IdentificationCertificate;
import com.example.SWP.entity.KoiType;
import com.example.SWP.model.request.CertificateRequest;
import com.example.SWP.model.request.KoiTypeRequest;
import com.example.SWP.model.response.CertificateResponse;
import com.example.SWP.model.response.KoiTypeResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Certificate")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CertificateAPI {

    @Autowired
    CertificateService certificateService;

    @PostMapping()
    public ResponseEntity newCertificate(@Valid @RequestBody CertificateRequest certificateRequest ) {
        CertificateResponse certificateResponse = certificateService.createCertificate(certificateRequest);
        return ResponseEntity.ok(certificateResponse);
    }

    @GetMapping()
    public ResponseEntity getAllCertificate(){
        List<CertificateResponse> certificateList = certificateService.getAllCertificate();
        return ResponseEntity.ok(certificateList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateCertificate(@Valid @RequestBody CertificateRequest certificateRequest, @PathVariable Long id){
        try{
            CertificateResponse newCertificate = certificateService.updateCertificate(id, certificateRequest);
            return ResponseEntity.ok(newCertificate);
        }catch (Exception e){
            throw new RuntimeException("Id of certificate " + id + " not found ");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCertificate(@PathVariable Long id){
        try{
            IdentificationCertificate oldCertificate = certificateService.deleteCertificate(id);
            return ResponseEntity.ok(oldCertificate);
        }catch (Exception e){
            throw new RuntimeException("Id of certificate : " + id + " not found");
        }
    }
}
