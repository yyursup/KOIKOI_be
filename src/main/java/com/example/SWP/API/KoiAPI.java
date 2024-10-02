package com.example.SWP.API;

import com.example.SWP.Service.KoiService;
import com.example.SWP.entity.Koi;
import com.example.SWP.model.KoiRequest;
import com.example.SWP.model.KoiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Koi")
@CrossOrigin(origins = "http://localhost:5175")
@SecurityRequirement(name = "api")
public class KoiAPI {

    @Autowired
    KoiService koiService;

    @PostMapping()
    public ResponseEntity newKoiFish(@Valid @RequestBody KoiRequest koiRequest ) {
        KoiResponse newKoi = koiService.createKoi(koiRequest);
        return ResponseEntity.ok(newKoi);
    }

    @GetMapping()
    public ResponseEntity getAllKoi(){
        List<KoiResponse> koiList = koiService.getAllKoi();
        return ResponseEntity.ok(koiList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateKoi(@Valid @RequestBody KoiRequest koiRequest, @PathVariable Long id){
        try{
            KoiResponse newKoi = koiService.updateKoi(id, koiRequest);
            return ResponseEntity.ok(newKoi);
        }catch (Exception e){
            throw new RuntimeException("Id of koi " + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoi(@PathVariable Long id){
        try{
            KoiResponse oldKoi = koiService.deleteKoi(id);
            return ResponseEntity.ok(oldKoi);
        }catch (Exception e){
            throw new RuntimeException("Id of koi : " + id + " not found");
        }
    }

}
