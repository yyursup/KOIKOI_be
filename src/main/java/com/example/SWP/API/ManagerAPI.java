package com.example.SWP.API;

import com.example.SWP.Service.ManagerService;
import com.example.SWP.model.request.RegisterRequest;
import com.example.SWP.model.request.UpdateProfileRequest;
import com.example.SWP.model.response.RegisterResponse;
import com.example.SWP.model.response.UpdateAndDeleteProfileResponse;
import com.example.SWP.model.response.ViewProfileResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Manager")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ManagerAPI {

    @Autowired
    ManagerService managerService;

    @PostMapping("registerForManager")
    public ResponseEntity registerManager(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse newAccount = managerService.registerForManager(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("Profile")
    public ResponseEntity ViewProfile(){
        ViewProfileResponse view = managerService.viewProfile();
        return ResponseEntity.ok(view);
    }


    @PutMapping("{id}")
    public ResponseEntity updateAccount(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, @PathVariable Long id){
        try{
            UpdateAndDeleteProfileResponse newAccount = managerService.accountUpdate(updateProfileRequest,id);
            return ResponseEntity.ok(newAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account " + id + " not found ");
        }

    }


}
