package com.example.SWP.API;

import com.example.SWP.Service.StaffService;
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

import java.util.List;

@RestController
@RequestMapping("/api/Staff")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class StaffAPI {

    @Autowired
    StaffService staffService;

    @PostMapping("StaffAccount")
    public ResponseEntity registerStaff(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse newAccount = staffService.registerForStaff(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping()
    public ResponseEntity getAllList(){
        List<ViewProfileResponse> accountList = staffService.getAllAccount();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("Profile")
    public ResponseEntity ViewProfile(){
        ViewProfileResponse view = staffService.viewProfile();
        return ResponseEntity.ok(view);
    }


    @PutMapping("{id}")
    public ResponseEntity updateAccount(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, @PathVariable Long id){
        try{
            UpdateAndDeleteProfileResponse newAccount = staffService.accountUpdate(updateProfileRequest,id);
            return ResponseEntity.ok(newAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account " + id + " not found ");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteAccount(@PathVariable Long id){
        try{
            UpdateAndDeleteProfileResponse oldAccount = staffService.deleteAccount(id);
            return ResponseEntity.ok(oldAccount);
        }catch (Exception e){
            throw new RuntimeException("Id of account : " + id + " not found");
        }
    }

}
