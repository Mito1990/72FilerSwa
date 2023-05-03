package com.swa.filter.RestController;

import com.swa.filter.Services.MyGroupService;
import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.MyGroupDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/")
public class MyGroupController{
  private final MyGroupService myGroupService;

  @GetMapping(path = "/groups/get/group")
  public ResponseEntity<?>getGroup(@RequestParam String groupname){
    MyGroups group = myGroupService.getGroup(groupname);
    if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupname+"} doesn't exists!");
    return ResponseEntity.ok().body(group);
  }
  @GetMapping(path = "/groups/get/all")
  public ResponseEntity<List<MyGroups>>getAllGroups(){
    return ResponseEntity.ok().body(myGroupService.getAllGroups());
  }
  @PostMapping(path = "/groups/delete/user")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody MyGroupDetails userGroupInfo){
    return ResponseEntity.ok().body(myGroupService.deleteMemberFromGroup(userGroupInfo));
  }
  
  @PostMapping(path = "/groups/create/group")
  public ResponseEntity<?> createGroup(@RequestParam String groupname){
    return ResponseEntity.ok().body(myGroupService.createGroup(groupname));
  }  

  @PostMapping(path = "/groups/add/user/to/group")
  public ResponseEntity<?> addUserToGroup(@RequestBody MyGroupDetails userGroupInfo){
    return ResponseEntity.ok().body(myGroupService.addUserToGroup(userGroupInfo));
  }
}

