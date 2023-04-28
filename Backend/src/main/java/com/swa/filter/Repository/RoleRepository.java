package com.swa.filter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swa.filter.mySQLTables.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);
}
