package com.example.SWP.API;

import com.example.SWP.Service.KoiService;
import com.example.SWP.entity.IdentificationCertificate;
import com.example.SWP.entity.Koi;
import com.example.SWP.model.request.KoiRequest;
import com.example.SWP.model.response.KoiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Koi")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiAPI {

    @Autowired
    KoiService koiService;

    @PostMapping("{id}/{idCertificate}")
    public ResponseEntity newKoiFish(@Valid @RequestBody KoiRequest koiRequest, @PathVariable Long id, @PathVariable Long idCertificate ) {
        KoiResponse newKoi = koiService.createKoi(koiRequest, id, idCertificate);
        return ResponseEntity.ok(newKoi);
    }

    @GetMapping()
    public ResponseEntity getAllKoi(){
        List<KoiResponse> koiList = koiService.getAllKoi();
        return ResponseEntity.ok(koiList);
    }

    @PutMapping("{id}/{id2}")
    public ResponseEntity updateKoi(@Valid @RequestBody KoiRequest koiRequest, @PathVariable Long id, @PathVariable Long id2){
            KoiResponse newKoi = koiService.updateKoi(id, koiRequest, id2);
            return ResponseEntity.ok(newKoi);

    }

    @GetMapping("/certificate")
    public IdentificationCertificate getCertificateByKoi(@RequestParam long id) {
        return koiService.getCertificateByKoiId(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoi(@PathVariable Long id){
        try{
            Koi oldKoi = koiService.deleteKoi(id);
            return ResponseEntity.ok(oldKoi);
        }catch (Exception e){
            throw new RuntimeException("Id of koi : " + id + " not found");
        }
    }

}
