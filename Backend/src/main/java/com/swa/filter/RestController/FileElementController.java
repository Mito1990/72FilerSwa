package com.swa.filter.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa.filter.ObjectModel.CreateNewFileRequest;
import com.swa.filter.ObjectModel.CreateNewFolderRequest;
import com.swa.filter.ObjectModel.DeleteFileRequest;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.ReadFileRequest;
import com.swa.filter.ObjectModel.RenameFileRequest;
import com.swa.filter.ObjectModel.RenameMemberGroupRequest;
import com.swa.filter.ObjectModel.WriteFileRequest;
import com.swa.filter.Services.FileElementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folder")
public class FileElementController {
    private final FileElementService fileElementService;

    @PostMapping(path = "/createNewFolder")
    public ResponseEntity<?> createNewFolder(@RequestBody CreateNewFolderRequest createNewFolderRequest) throws JsonProcessingException{
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(fileElementService.createNewFolder(createNewFolderRequest));
      return ResponseEntity.ok().body(jsonString);
    }
    @PostMapping("/getFolder")
    public ResponseEntity<?> getFolder(@RequestBody GetFolderRequest getFolderRequest) throws JsonProcessingException{
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(fileElementService.getFolder(getFolderRequest));
      return ResponseEntity.ok().body(jsonString);
    }
    @PostMapping("/createNewFile")
    public ResponseEntity<?> createNewFile(@RequestBody CreateNewFileRequest CreateNewFileRequest) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(ResponseEntity.ok().body(fileElementService.createNewFile(CreateNewFileRequest)));
        return ResponseEntity.ok().body(jsonString);
    }
    @PostMapping("/file/read")
    public String readFile(@RequestBody ReadFileRequest groupRequest) throws JsonProcessingException, java.io.IOException  {
        return fileElementService.readFile(groupRequest);
    }

    @PostMapping("/file/write")
    public void writeFile(@RequestBody WriteFileRequest writeFileRequest) throws JsonProcessingException, java.io.IOException  {
        fileElementService.writeFile(writeFileRequest);
    }
    @PostMapping("/file/delete")
    public ResponseEntity<?> deleteFile(@RequestBody DeleteFileRequest groupRequest) throws JsonProcessingException, java.io.IOException  {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(ResponseEntity.ok().body(fileElementService.deleteFile(groupRequest)));
      return ResponseEntity.ok().body(jsonString);
    }
    @PostMapping("/file/rename/fileElement")
    public ResponseEntity<?>renameFileElement(@RequestBody RenameFileRequest renameFileRequest) throws JsonProcessingException, java.io.IOException  {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(ResponseEntity.ok().body(fileElementService.renameFileElement(renameFileRequest)));
      return ResponseEntity.ok().body(jsonString);
    }
    @PostMapping("/file/rename/group")
    public ResponseEntity<?>renameMemberGroup(@RequestBody RenameMemberGroupRequest renameMemberGroupRequest) throws JsonProcessingException, java.io.IOException  {
      System.out.println("\n\n\n\nhello from FileElementController -> renameMemberGroup\n\n\n\n");
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(ResponseEntity.ok().body(fileElementService.renameMemberGroup(renameMemberGroupRequest)));
      return ResponseEntity.ok().body(jsonString);
    }
    // @PostMapping("/file/rename")
    // public ResponseEntity<?> renameFile(@RequestBody GroupRequest writeFileRequest) throws JsonProcessingException, java.io.IOException  {
    //     return ResponseEntity.ok().body(fileElementService.renameFile(writeFileRequest));
    // }
    // @PostMapping("/new")
    //     public ResponseEntity<?> newFolder(@RequestBody NewFolderRequest newFolderRequest){
    //     return ResponseEntity.ok().body(fileService.newFo(newFolderRequest));
    // }
 
    // @PostMapping("/new")
    // public ResponseEntity<?> newFolder(@RequestBody NewFolderRequest newFolderRequest){
    //     return ResponseEntity.ok().body(fileService.newFolder(newFolderRequest));
    // }
    // @PostMapping("/get/all")
    // public ResponseEntity<?> getAll(@RequestBody GetFolderRequest getFolderRequest){
    //     return ResponseEntity.ok().body(fileService.getALLFoldersUser(getFolderRequest));
    // }
    // @PostMapping("/get/share")
    // public ResponseEntity<?> getSharedFolders(@RequestBody GetFolderRequest getFolderRequest){
    //     return ResponseEntity.ok().body(fileService.getListOfSharedFolderID(getFolderRequest));
    // }
   
    // @PostMapping("/file")
    // public ResponseEntity<String> readFile(@RequestBody GroupRequest groupRequest) throws JsonProcessingException, java.io.IOException  {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     String jsonString = objectMapper.writeValueAsString(ResponseEntity.ok().body(fileService.readFile(groupRequest)));
    //     return ResponseEntity.ok().body(jsonString);
    // }

}
