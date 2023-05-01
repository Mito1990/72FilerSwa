package com.swa.filter.RestController;

import com.swa.filter.Services.RoleService;
import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/")
public class RoleController {
  private final UserService userService;
  private final RoleService roleService;
  @PostMapping(path = "/roles/add/role")
  public ResponseEntity<?> addRole(@RequestBody Role role){
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/add/role").toUriString());
    if(roleService.getRole(role.getName())!=null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rule with name:{"+role.getName()+"} exists already in table:{"+roleService.getRole(role.getName())+"}");
    else if(role.getName().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field name is empty!");
    else return ResponseEntity.created(uri).body(roleService.addRole(role));
  }

  @PostMapping(path = "/roles/add/role/to/user")
  public ResponseEntity<RoleToUser> addRoleToUser(@RequestBody RoleToUser roleToUser){
    userService.addRoleToUser(roleToUser.getUsername(), roleToUser.getRole());
    return ResponseEntity.ok().build();
  }
  @GetMapping(path = "/roles/get/all")
  public ResponseEntity<List<Role>>getAllRoles(){
    return ResponseEntity.ok().body(roleService.getAllRoles());
  }
  @GetMapping(path = "/roles/get/role")
  public ResponseEntity<Role>getRole(Role role){
    return ResponseEntity.ok().body(roleService.getRole(role.getName()));
  }

}

