package com.swa.filter.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa.filter.ObjectModel.CreateNewFileRequest;
import com.swa.filter.ObjectModel.CreateNewFolderRequest;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GetFolderResponse;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.ObjectModel.WriteFileRequest;
import com.swa.filter.Services.FileElementService;
import com.swa.filter.mySQLTables.Folder;

import io.jsonwebtoken.io.IOException;
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
    // @PostMapping("/file/read")
//     public String readFile(@RequestBody GroupRequest groupRequest) throws JsonProcessingException, java.io.IOException  {
//         return fileService.readFile(groupRequest);
//     } 
//     @PostMapping("/file/write")
//     public void writeFile(@RequestBody WriteFileRequest writeFileRequest) throws JsonProcessingException, java.io.IOException  {
//         fileService.writeFile(writeFileRequest);
//     }
//     @PostMapping("/file/delete")
//     public ResponseEntity<?> deleteFile(@RequestBody GroupRequest groupRequest) throws JsonProcessingException, java.io.IOException  {
//         return ResponseEntity.ok().body(fileService.deleteFile(groupRequest));
//     }
//     @PostMapping("/file/rename")
//     public ResponseEntity<?> renameFile(@RequestBody GroupRequest writeFileRequest) throws JsonProcessingException, java.io.IOException  {
//         return ResponseEntity.ok().body(fileService.renameFile(writeFileRequest));
//     }
}
