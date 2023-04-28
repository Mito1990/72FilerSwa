package com.swa.filter.Services;

import com.swa.filter.Interface.RoleServiceInterface;
import com.swa.filter.Repository.RoleRepository;
import com.swa.filter.mySQLTables.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleService implements RoleServiceInterface {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role addRole(Role name){
        log.info("Saving role:{} to the Database", name.getName());
        return roleRepository.save(name);
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name);
    }

}
