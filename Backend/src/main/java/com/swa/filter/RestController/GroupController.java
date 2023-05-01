package com.swa.filter.RestController;

import com.swa.filter.Services.MyGroupService;
import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.UserGroupInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/")
public class GroupController{
  private final UserService userService;
  private final MyGroupService myGroupService;

  @GetMapping(path = "/groups/get/group")
  public ResponseEntity<?>getGroup(String groupName){
    MyGroups group = myGroupService.getGroup(groupName);
    if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupName+"} doesn't exists!");
    return ResponseEntity.ok().body(group);
  }
  @GetMapping(path = "/groups/get/all")
  public ResponseEntity<List<MyGroups>>getAllGroups(){
    return ResponseEntity.ok().body(myGroupService.getAllGroups());
  }
  @PostMapping(path = "/groups/delete/user")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody UserGroupInfo userGroupInfo){
    if(!myGroupService.checkIfGroupExists(userGroupInfo.getGroupName()))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+userGroupInfo.getGroupName()+"} doesn't exists!");
    if(ResponseEntity.ok().body(myGroupService.deleteMemberFromGroup(userGroupInfo))==null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User:{"+userGroupInfo.getUserName()+"} in Group:{"+userGroupInfo.getGroupName()+"} doesn't exists!");
    else return ResponseEntity.ok().body(myGroupService.deleteMemberFromGroup(userGroupInfo));
  }
  
  @PostMapping(path = "/groups/create/group")
  public ResponseEntity<?> createGroup(@RequestBody MyGroups newgroup){
    if(newgroup.getGroupName().isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group Field is empty!");
    if(!myGroupService.checkIfGroupExists(newgroup.getGroupName()))return ResponseEntity.ok().body(myGroupService.createGroup(newgroup));
    else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+newgroup.getGroupName()+"} exists already!");
  }  
  @PostMapping(path = "/groups/add/user/to/group")
  public ResponseEntity<?> addUserToGroup(@RequestBody UserGroupInfo userGroupInfo){
    if(!userService.checkIfUserExists(userGroupInfo.getUserName()))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User:{"+userGroupInfo.getUserName()+"} doesn't exists!");
    if(!myGroupService.checkIfGroupExists(userGroupInfo.getGroupName()))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+userGroupInfo.getGroupName()+"} doesn't exists!");
    if(myGroupService.checkIfUserExistsInGroup(userGroupInfo))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User:{"+userGroupInfo.getUserName()+"} exists already in Group:{"+userGroupInfo.getGroupName()+"}");
    else return ResponseEntity.ok().body(myGroupService.addUserToGroup(userGroupInfo));
  }
}

