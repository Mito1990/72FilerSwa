package com.swa.filter.RestController;

import com.swa.filter.Services.MyGroupService;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.MyGroupDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/groups")
public class MyGroupController{
  private final MyGroupService myGroupService;

  @GetMapping(path = "/get/group")
  public ResponseEntity<?>getGroup(@RequestParam String groupname){
    MyGroups group = myGroupService.getGroup(groupname);
    if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupname+"} doesn't exists!");
    return ResponseEntity.ok().body(group);
  }
  @GetMapping(path = "/get/all")
  public ResponseEntity<List<MyGroups>>getAllGroups(){
    return ResponseEntity.ok().body(myGroupService.getAllGroups());
  }
  @PostMapping(path = "/delete/user")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody MyGroupDetails userGroupInfo){
    return ResponseEntity.ok().body(myGroupService.deleteMemberFromGroup(userGroupInfo));
  }
  
  @PostMapping(path = "/create/group")
  public ResponseEntity<?> createGroup(@RequestParam String groupname){
    return ResponseEntity.ok().body(myGroupService.createGroup(groupname));
  }  

  @PostMapping(path = "/add/user/to/group")
  public ResponseEntity<?> addUserToGroup(@RequestBody MyGroupDetails userGroupInfo){
    return ResponseEntity.ok().body(myGroupService.addUserToGroup(userGroupInfo));
  }
}

