package com.example.SWP.API;

import com.example.SWP.Service.KoiService;
import com.example.SWP.entity.Koi;
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

    @PostMapping("newKoiFish")
    public ResponseEntity newKoiFish(@Valid @RequestBody Koi koi ) {
        Koi newKoi = koiService.createKoi(koi);
        return ResponseEntity.ok(newKoi);
    }

    @GetMapping("AllKoi")
    public ResponseEntity getAllKoi(){
        List<Koi> koiList = koiService.getAllKois();
        return ResponseEntity.ok(koiList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateKoi(@Valid @RequestBody Koi koiUpdate, @PathVariable long id){
        try{
            Koi newKoi = koiService.updateKoi(id, koiUpdate);
            return ResponseEntity.ok(newKoi);
        }catch (Exception e){
            throw new RuntimeException("Id of koi " + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoi(@PathVariable long id){
        try{
            Koi oldKoi = koiService.deleteKoi(id);
            return ResponseEntity.ok(oldKoi);
        }catch (Exception e){
            throw new RuntimeException("Id of koi : " + id + " not found");
        }
    }

}
