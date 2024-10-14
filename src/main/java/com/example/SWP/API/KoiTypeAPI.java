package com.example.SWP.API;


import com.example.SWP.Service.KoiTypeService;
import com.example.SWP.entity.KoiType;
import com.example.SWP.model.Request.KoiTypeRequest;
import com.example.SWP.model.Response.KoiTypeResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/KoiTypes")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiTypeAPI {
    @Autowired
    KoiTypeService koiTypeService;

    @PostMapping()
    public ResponseEntity newKoiType(@Valid @RequestBody KoiTypeRequest koiTypeRequest ) {
        KoiTypeResponse koiTypeResponse = koiTypeService.createKoiTypes(koiTypeRequest);
        return ResponseEntity.ok(koiTypeResponse);
    }

    @GetMapping()
    public ResponseEntity getAllKoiTypes(){
        List<KoiTypeResponse> koiTypeList = koiTypeService.getAllKoiTypes();
        return ResponseEntity.ok(koiTypeList);
    }

    @PutMapping("{id}")
    public ResponseEntity updateKoiTypes(@Valid @RequestBody KoiTypeRequest koiTypeRequest, @PathVariable Long id){
        try{
            KoiTypeResponse newKoiTypes = koiTypeService.updateKoiTypes(id, koiTypeRequest);
            return ResponseEntity.ok(newKoiTypes);
        }catch (Exception e){
            throw new RuntimeException("Id of koi " + id + " not found ");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteKoiTypes(@PathVariable Long id){
        try{
            KoiType oldKoiType = koiTypeService.deleteKoiTypes(id);
            return ResponseEntity.ok(oldKoiType);
        }catch (Exception e){
            throw new RuntimeException("Id of koi : " + id + " not found");
        }
    }
}