package com.example.demo.api;

import com.example.demo.entity.Koi;
import com.example.demo.service.KoiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*") //cho phép tất cả truy cập
@RequestMapping("/api/koi")
@SecurityRequirement(name = "api")
public class KoiAPI {

    @Autowired
    KoiService koiService;

    @GetMapping
    public ResponseEntity getAll() {
        List<Koi> kois = koiService.getAll();
        return ResponseEntity.ok(kois);
    }

    @PostMapping
    public  ResponseEntity create(@RequestBody Koi koi) {
        Koi newKoi = koiService.create(koi);
        return ResponseEntity.ok(newKoi);
    }

}
