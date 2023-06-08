package com.swa.filter.RestController;

import com.swa.filter.ObjectModel.AddUserToMemberGroupRequest;
import com.swa.filter.ObjectModel.CreateMemberGroupRequest;
import com.swa.filter.ObjectModel.DeleteMemberFromGroupRequest;
import com.swa.filter.ObjectModel.DeleteMemberGroupRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa.filter.ObjectModel.GetListOfMemberGroupsRequest;
import com.swa.filter.Services.MemberGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/memberGroupController")
public class MemberController{
  private final MemberGroupService memberService;

  @PostMapping(path = "/createMemberGroup")
  public ResponseEntity<?> createMemberGroup(@RequestBody CreateMemberGroupRequest createMemberGroupRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.createMemberGroup(createMemberGroupRequest));
    return ResponseEntity.ok().body(jsonString);
  }
  @PostMapping(path = "/getListOfMemberGroups")
  public ResponseEntity<?> getListOfMemberGroups(@RequestBody GetListOfMemberGroupsRequest getListOfMemberGroupsRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.getListOfGroupsIncludeUser(getListOfMemberGroupsRequest));
    return ResponseEntity.ok().body(jsonString);
  }
  @PostMapping(path = "/addUserToMemberGroup")
  public ResponseEntity<?> addUserToMemberGroup(@RequestBody AddUserToMemberGroupRequest addUserToMemberGroupRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.addUserToMemberGroup(addUserToMemberGroupRequest));
    return ResponseEntity.ok().body(jsonString);
  }
  @PostMapping(path = "/deleteMemberFromGroup")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody DeleteMemberFromGroupRequest deleteMemberFromGroupRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.deleteMemberFromGroup(deleteMemberFromGroupRequest));
    return ResponseEntity.ok().body(jsonString);
  }
  @PostMapping(path = "/deleteMemberGroup")
  public ResponseEntity<?> deleteMemberFromGroup(@RequestBody  DeleteMemberGroupRequest  deleteMemberGroupRequest) throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(memberService.deleteMemberGroup(deleteMemberGroupRequest));
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

