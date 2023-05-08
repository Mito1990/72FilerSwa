package com.swa.filter.RestController;

import com.swa.filter.ObjectModel.MemberGroupRequest;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.Services.MyGroupService;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.MyGroupMembers;

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
  public ResponseEntity<?>getGroup(@RequestBody GroupRequest groupRequest){
    MyGroups group = myGroupService.getGroup(groupRequest);
    if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupRequest.getGroupname()+"} doesn't exists!");
    return ResponseEntity.ok().body(group);
  }
  @GetMapping(path = "/get/all")
  public ResponseEntity<List<MyGroups>>getAllGroups(){
    return ResponseEntity.ok().body(myGroupService.getAllGroups());
  }
  @DeleteMapping(path = "/delete/user")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody MemberGroupRequest memberGroupRequest){
    return ResponseEntity.ok().body(myGroupService.deleteMemberFromGroup(memberGroupRequest));
  }
  
  @PostMapping(path = "/create/group")
  public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest){
    return ResponseEntity.ok().body(myGroupService.createGroup(groupRequest));
  }  

  @PostMapping(path = "/add/user/to/group")
  public ResponseEntity<?> addUserToGroup(@RequestBody MemberGroupRequest memberGroupRequest){
    System.out.println("Test from Controller");
    System.out.println(memberGroupRequest);
    return ResponseEntity.ok().body(myGroupService.addUserToGroup(memberGroupRequest));
  }
}

