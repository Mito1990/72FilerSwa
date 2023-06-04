package com.swa.filter.RestController;

import com.swa.filter.ObjectModel.NewShareFolderRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa.filter.ObjectModel.AddFolderToGroupRequest;
import com.swa.filter.ObjectModel.AddNewFolderRequest;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GroupFolderRequest;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.Services.MemberService;
import com.swa.filter.mySQLTables.Folder;

import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/share")
public class MemberController{
  private final MemberService memberService;

  @PostMapping(path = "/newShareFolder")
  public ResponseEntity<?> newShareFolder(@RequestBody NewShareFolderRequest newShareFolderRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.newShareFolder(newShareFolderRequest));
    return ResponseEntity.ok().body(jsonString);
  }
  @PostMapping(path = "/addNewFolder")
  public ResponseEntity<?> addNewFolder(@RequestBody AddNewFolderRequest addNewFolderRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.addNewFolder(addNewFolderRequest));
    return ResponseEntity.ok().body(jsonString);
  }

  @PostMapping(path = "/getFolder")
  public ResponseEntity<?> getFolder(@RequestBody GetFolderRequest getFolderRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.getFolder(getFolderRequest));
    return ResponseEntity.ok().body(jsonString);
  }





  // @PostMapping(path = "/get/group")
  // public ResponseEntity<?>getGroup(@RequestBody GroupRequest groupRequest) throws JsonProcessingException{
  //   List<MyGroups> group = memberService.getGroup(groupRequest);
  //   // if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupRequest.getGroupname()+"} doesn't exists!");
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(group);
  //   return ResponseEntity.ok().body(jsonString);
  // }
  // @GetMapping(path = "/get/all")
  // public ResponseEntity<List<MyGroups>>getAllGroups(){
  //   return ResponseEntity.ok().body(memberService.getAllGroups());
  // }
  // @DeleteMapping(path = "/delete/user")
  // public ResponseEntity<?> deleteMemberFromGroup(@RequestBody NewShareFolderRequest newShareFolderRequest){
  //   return ResponseEntity.ok().body(memberService.deleteMemberFromGroup(newShareFolderRequest));
  // }
  // @PostMapping(path = "/create/group")
  // public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest) throws JsonProcessingException{
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(memberService.createGroup(groupRequest));
  //   return ResponseEntity.ok().body(jsonString);
  // }

  // @PostMapping(path = "/add/user/to/group")
  // public ResponseEntity<?> addUserToGroup(@RequestBody NewShareFolderRequest newShareFolderRequest) throws JsonProcessingException{
  //   System.out.println("Test from Controller");
  //   System.out.println(newShareFolderRequest);
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(memberService.addUserToGroup(newShareFolderRequest));
  //   return ResponseEntity.ok().body(jsonString);
  // }
  // @PostMapping(path = "/get/group/folder")
  // public ResponseEntity<?>getFolderFromGroup(@RequestBody GroupRequest groupRequest) throws JsonProcessingException{
  //   List<Folder> folders = memberService.getFolderFromGroup(groupRequest);
  //   // if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupRequest.getGroupname()+"} doesn't exists!");
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(folders);
  //   return ResponseEntity.ok().body(jsonString);
  // }
  // @PostMapping(path = "/add/folder")
  // public ResponseEntity<?>addFolderToSharedFolder(@RequestBody GroupRequest groupRequest) throws JsonProcessingException{
  //   List<Folder> folders = memberService.addFolderToSharedFolder(groupRequest);
  //   // if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupRequest.getGroupname()+"} doesn't exists!");
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(folders);
  //   return ResponseEntity.ok().body(jsonString);
  // }
  
  // @PostMapping(path = "/get/group/folder/folder")
  // public ResponseEntity<?>getFolderFromGroupFolder(@RequestBody GroupRequest groupRequest) throws JsonProcessingException{
  //   List<FolderDir> folders = memberService.getFolderFromGroupFolder(groupRequest);
  //   // if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupRequest.getGroupname()+"} doesn't exists!");
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(folders);
  //   return ResponseEntity.ok().body(jsonString);
  // }
  // @PostMapping(path = "/add/folder/to/group")
  // public ResponseEntity<?>addFoldertoGroup(@RequestBody NewFolderGroupRequest addFolderToGroupRequest) throws JsonProcessingException{
  //   List<FolderDir> folders = memberService.addFolderToGroup(addFolderToGroupRequest);
  //   // if(group  == null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Group:{"+groupRequest.getGroupname()+"} doesn't exists!");
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   // Convert the object to JSON string
  //   String jsonString = objectMapper.writeValueAsString(folders);
  //   return ResponseEntity.ok().body(jsonString);
  // }
}

