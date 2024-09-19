package com.example.SWP.Repository;

import com.example.SWP.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleById(long id);
}
