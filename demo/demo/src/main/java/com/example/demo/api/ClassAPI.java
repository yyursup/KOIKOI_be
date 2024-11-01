package com.example.demo.api;

import com.example.demo.entity.ClassEntity;
import com.example.demo.model.ClassRequest;
import com.example.demo.service.ClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/class")
@SecurityRequirement(name = "api")
public class ClassAPI {

    @Autowired
    ClassService classService;

    @PostMapping
    public ResponseEntity createNewclass(@RequestBody ClassRequest classRequest) {
        ClassEntity classEntity = classService.createNewClass(classRequest);
        return ResponseEntity.ok(classEntity);
    }
}
