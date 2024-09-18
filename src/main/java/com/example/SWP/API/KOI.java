package com.example.SWP.API;

import com.example.SWP.entity.Account;
import com.example.SWP.entity.Koi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/koi")
@CrossOrigin("*")
public class KOI {

    @GetMapping("get")
    public ResponseEntity getAllAccount(){
     return ResponseEntity.ok("koi");
    }
}
