package com.swa.filter.Interface;

import java.util.List;
import com.swa.filter.mySQLTables.Role;

public interface RoleServiceInterface {
    List<Role>getAllRoles();
    Role addRole(Role name);
    Role getRole(String name);
}
