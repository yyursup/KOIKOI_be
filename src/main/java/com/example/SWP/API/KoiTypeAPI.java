package com.example.SWP.API;


import com.example.SWP.Service.KoiTypeService;
import com.example.SWP.entity.KoiType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/KoiType")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiTypeAPI {
    @Autowired
    KoiTypeService koiTypeService;

    @PostMapping("CreateKoiType")
    public ResponseEntity newKoiType(@Valid @RequestBody KoiType koiType ) {
        KoiType newKoiType = koiTypeService.createKoiTypes(koiType);
        return ResponseEntity.ok(newKoiType);
    }

    @GetMapping("AllKoiType")
    public ResponseEntity getAllKoiType(){
        List<KoiType> koiTypeList = koiTypeService.getAllKoiTypes();
        return ResponseEntity.ok(koiTypeList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateKoiType(@Valid @RequestBody KoiType koiIsUpdate, @PathVariable Long id){
        try{
            KoiType newKoiType = koiTypeService.updateKoiTypes(id, koiIsUpdate);
            return ResponseEntity.ok(newKoiType);
        }catch (Exception e){
            throw new RuntimeException("Id of koiType " + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoiType(@PathVariable Long id){
        try{
            KoiType oldKoiType = koiTypeService.deleteKoiTypes(id);
            return ResponseEntity.ok(oldKoiType);
        }catch (Exception e){
            throw new RuntimeException("Id of koiType : " + id + " not found");
        }
    }
}
