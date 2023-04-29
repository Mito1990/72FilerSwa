package com.swa.filter.RestController;

import com.swa.filter.Services.MyGroupService;
import com.swa.filter.Services.RoleService;
import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.Role;
import com.swa.filter.mySQLTables.User;
import com.swa.filter.mySQLTables.UserGroupInfo;

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
public class RestControllerApp {
  private final UserService userService;
  private final RoleService roleService;
  private final MyGroupService myGroupService;

  @GetMapping(path = "/users/get/all" )
  public ResponseEntity<List<User>>getAllUsers(){
    return ResponseEntity.ok().body(userService.getAllUsers());
  }
  @GetMapping(path = "/users/get/user" )
  public ResponseEntity<User>getUsers(String user){
    return ResponseEntity.ok().body(userService.getUser(user));
  }

  @PostMapping(path = "/users/add/user")
  public ResponseEntity<?> addUser(@RequestBody User user) {
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/add/user").toUriString());
    if(user.getName().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field name is empty!");
    else if(user.getUsername().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field username is empty!");
    else if(user.getPassword().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field password is empty!");
    else if(user.getPassword().length()<8)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password is to short enter at least 8 character");
    else if(userService.getUser(user.getUsername())!=null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User with username:{"+user.getUsername()+"} exists already in table:{"+userService.getUser(user.getUsername())+"}");
    else if(ResponseEntity.created(uri).body(userService.addUser(user))==null);
    return ResponseEntity.created(uri).body(userService.addUser(user));
  }

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
  @GetMapping(path = "/groups/get/group")
  public ResponseEntity<?>getGroup(String groupName){
    MyGroups group = myGroupService.getGroup(groupName);
    if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupName+"} doesn't exists!");
    return ResponseEntity.ok().body(myGroupService.getGroup(groupName));
  }
  @GetMapping(path = "/groups/get/all")
  public ResponseEntity<List<MyGroups>>getAllGroups(){
    return ResponseEntity.ok().body(myGroupService.getAllGroups());
  }
  @PostMapping(path = "/groups/delete/user")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody UserGroupInfo userGroupInfo){
    MyGroups mygroup = myGroupService.getGroup(userGroupInfo.getGroupName());
    if(!mygroup.getGroupName().equalsIgnoreCase(userGroupInfo.getGroupName()))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+userGroupInfo.getGroupName()+"} doesn't exists!");
    List<UserGroupInfo>getinfo = mygroup.getInfo();
    for (UserGroupInfo item : getinfo) {
        if(item.getUserName().equalsIgnoreCase(userGroupInfo.getUserName())){
          mygroup.getInfo().remove(getinfo.indexOf(item));
          return ResponseEntity.ok().body(myGroupService.deleteMemberFromGroup(userGroupInfo));
        };
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+userGroupInfo.getUserName()+"} exists already in Group:{"+userGroupInfo.getGroupName()+"}");
  }
  
  @PostMapping(path = "/groups/create/group")
  public ResponseEntity<?> createGroup(@RequestBody MyGroups newgroup){
    if(newgroup.getGroupName().isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group Field is empty!");
    List<MyGroups>mygroups = myGroupService.getAllGroups();
    for (MyGroups group : mygroups) {
        if(group.getGroupName().equalsIgnoreCase(newgroup.getGroupName()))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+group.getGroupName()+"} exists already!");
    }
    return ResponseEntity.ok().body(myGroupService.createGroup(newgroup));
  }  
  @PostMapping(path = "/groups/add/user/to/group")
  public ResponseEntity<?> addUserToGroup(@RequestBody UserGroupInfo userGroupInfo){
    MyGroups mygroup = myGroupService.getGroup(userGroupInfo.getGroupName());
    User user = userService.getUser(userGroupInfo.getUserName());
    if(!mygroup.getGroupName().equalsIgnoreCase(userGroupInfo.getGroupName()))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+userGroupInfo.getGroupName()+"} doesn't exists!");
    List<UserGroupInfo>getinfo = mygroup.getInfo();
    for (UserGroupInfo item : getinfo) {
        System.out.println(item);
        if(item.getUserName().equalsIgnoreCase(userGroupInfo.getUserName())){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+userGroupInfo.getUserName()+"} exists already in Group:{"+userGroupInfo.getGroupName()+"}");
        };
    }
    return ResponseEntity.ok().body(myGroupService.addUserToGroup(userGroupInfo));
  }
}

