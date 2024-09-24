package com.example.SWP.API;

import com.example.SWP.Service.RoleService;
import com.example.SWP.entity.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
@CrossOrigin("*")

public class RoleAPI {
    @Autowired
    RoleService roleService;

    @PostMapping("Create")
    public ResponseEntity createRole(@RequestBody Role role){
        Role newRole = roleService.createRole(role);
        return ResponseEntity.ok(newRole);
    }

    @PutMapping("{id}")
    public ResponseEntity updateRole(@RequestBody Role role,@PathVariable long id){
        try{
            Role oldRole = roleService.updateRole(role,id);
            return ResponseEntity.ok(oldRole);
        }catch (Exception e){
            throw new RuntimeException("Can not found roleID");
        }
    }


}
