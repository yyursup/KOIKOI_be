package com.example.SWP.Service;

import com.example.SWP.Repository.RoleRepository;
import com.example.SWP.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Role updateRole(Role role,long roleID){
        Role oldRole = roleRepository.findRoleById(roleID);

        if(oldRole == null){
            throw new RuntimeException("Can not found this roleID");
        }
        oldRole.setRoleName(role.getRoleName());
        oldRole.setRoleType(role.getRoleType());
        return roleRepository.save(oldRole);
    }

//    public Role deleteRole(long roleID){
//        Role oldRole = roleRepository.findRoleById(roleID);
//
//        if(oldRole == null){
//            throw new RuntimeException("can not found this roleID");
//
//        }
//        oldRole.setDeleted(true);
//        return roleRepository.save(oldRole);
//    }

    public Role createRole(Role role){
        Role newRole = roleRepository.save(role);
        return newRole;
    }
}
