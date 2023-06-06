package com.swa.filter.Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Member;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.swa.filter.mySQLTables.FileElement;
import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.MemberGroup;
import com.swa.filter.mySQLTables.MyFile;
import com.swa.filter.mySQLTables.User;
import jakarta.transaction.Transactional;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GetFolderResponse;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.ObjectModel.ReadFileRequest;
import com.swa.filter.ObjectModel.RenameFileRequest;
import com.swa.filter.ObjectModel.RenameMemberGroupRequest;
import com.swa.filter.ObjectModel.WriteFileRequest;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.MemberGroupRepository;
import com.swa.filter.Repository.MyFileRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.RestController.FileElementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.swa.filter.ObjectModel.CreateNewFileRequest;
import com.swa.filter.ObjectModel.CreateNewFolderRequest;
import com.swa.filter.ObjectModel.DeleteFileRequest;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FileElementService {
  private final String rootUsers = "../users/";
  private final String pathHome = "/home/";
  private final String rootGroups = "../groups/";
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final FolderRepository folderRepository;
  private final MemberGroupRepository memberGroupRepository;
  private final MyFileRepository myFileRepository;
  private final FileElementRepository fileElementRepository;

  public void createUserFolder(String username) {
    Path userPathHome = Paths.get(rootUsers+username+pathHome);
    try {
      Files.createDirectories(userPathHome);
      System.out.println("Directory is created!");
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }
    log.info("userPathHome: {}",userPathHome);
  }
  public String createMemberGroupFolder(String groupName,Integer memberGroupID) {
    Path groupPath = Paths.get(rootGroups+memberGroupID.toString()+"_"+groupName);
    try {
      Files.createDirectories(groupPath);
      System.out.println("Directory is created!");
      return rootGroups+memberGroupID.toString()+"_"+groupName;
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }
    log.info("create member group folder: {}",groupPath);
    return "Something went wrong with creating a group path!";
  }

  public Folder createNewFolder(CreateNewFolderRequest createNewFolderRequest){
    System.out.println("\n\n\nCreateNewFolderRequest");
    System.out.println("-----------------------------------------------------");
    System.out.println("createNewFolderRequest");
    System.out.println(createNewFolderRequest.toString());
    if(createNewFolderRequest.getParentFolderID()==null){
        System.err.println("Parent == null");
        return null;
    }
    Optional<Folder> parentFolder = folderRepository.findById(createNewFolderRequest.getParentFolderID());
    Folder newFolder = new Folder(createNewFolderRequest.getFolderName(),createNewFolderRequest.getParentFolderID() ,createNewFolderRequest.getIsShared(),false);
    parentFolder.get().getChildren().add(newFolder);
    folderRepository.save(newFolder);
    // createNewFolderInUserFolder(createNewFolderRequest);
    System.out.println("new Folder is created!");
    System.out.println("-----------------------------------------------------\n\n\n");
    return parentFolder.get();
  }

  public Folder getFolder(GetFolderRequest getFolderRequest){
    System.out.println("getFolder");
    System.out.println("-----------------------------------------------------");
    System.out.println("getFolderRequest");
    System.out.println(getFolderRequest);
    Optional<Folder> parentFolder = folderRepository.findById(getFolderRequest.getParentID());
    System.out.println("-----------------------------------------------------\n");
    return parentFolder.get();
  }

  public Folder createNewFile(CreateNewFileRequest createNewFileRequest){
    System.out.println("\n\n\ncreateNewFile");
    System.out.println("----------------------------------------------");
    System.out.println(createNewFileRequest);
    Optional<Folder> parentFolder = folderRepository.findById(createNewFileRequest.getParentFolderID());
    MyFile newFile = new MyFile(createNewFileRequest.getFileName(),createNewFileRequest.getParentFolderID(),createNewFileRequest.getIsShared(),true);
    myFileRepository.save(newFile);
    String filePath = createNewFileInUserFolder(createNewFileRequest,newFile.getId());
    newFile.setPath(filePath);
    myFileRepository.save(newFile);
    parentFolder.get().getChildren().add(newFile);
    folderRepository.save(parentFolder.get());
    System.out.println("----------------------------------------------\n\n\n");
    return parentFolder.get();
  }

  public String createNewFileInUserFolder(CreateNewFileRequest createNewFileRequest,Integer fileID){
    String filePath;
    String owner = jwtService.extractUsername(createNewFileRequest.getToken());
    if(createNewFileRequest.getIsShared()){
      filePath = rootGroups+createNewFileRequest.getGroupID().toString()+"_"+createNewFileRequest.getGroupName()+"/"+fileID.toString()+"_"+createNewFileRequest.getFileName()+".txt";
    }else{
      filePath = rootUsers+owner+pathHome+fileID.toString()+"_"+createNewFileRequest.getFileName()+".txt";
    }
    File file = new File(filePath);
    try {
        boolean isNewFileCreated = file.createNewFile();
        if (isNewFileCreated) {
            System.out.println("File created successfully.");
        } else {
            System.out.println("File already exists.");
        }
    } catch (IOException e) {
        System.out.println("An error occurred while creating the file: " + e.getMessage());
    }
    return filePath;
}

public String readFile(ReadFileRequest readFileRequest) {
    System.out.println("\n\n\n\n\nreadFile");
      System.out.println("-------------------------------------");
      System.out.println("readRequest");
      System.out.println(readFileRequest);
      Optional<MyFile> readFile = myFileRepository.findById(readFileRequest.getFileID());
      String filePath = readFile.get().getPath();
      System.out.println("filePath");
      System.out.println(filePath);
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    System.out.println("-------------------------------------\n\n\n\n\n");

    return content.toString();
}
public void writeFile(WriteFileRequest writeFileRequest) {
  System.out.println("\n\n\nwriteFile");
  System.out.println("-------------------------------------");
  System.out.println("groupRequest");
  System.out.println(writeFileRequest);
  Optional<MyFile> writeFile = myFileRepository.findById(writeFileRequest.getFileID());
  String filePath = writeFile.get().getPath();
  System.out.println("-------------------------------------");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(writeFileRequest.getContent());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public String deleteFile(DeleteFileRequest deleteFileRequest){
  System.out.println("\n\n\nDeleteFile");
  System.out.println("-------------------------------------");
  System.out.println("deleteFileRequest");
  System.out.println(deleteFileRequest);
  Optional<MyFile> deleteFile = myFileRepository.findById(deleteFileRequest.getFolderID());
  String filePath = deleteFile.get().getPath();
  File file = new File(filePath);
  if (file.exists()) {
      boolean deleted = file.delete();
      if (deleted) {
          System.out.println("File deleted successfully.");
      } else {
          System.out.println("Failed to delete the file.");
      }
  } else {
      System.out.println("File does not exist.");
  }
  Optional<Folder> parent = folderRepository.findById(deleteFile.get().getParent());
  if(parent.get().getChildren().remove(deleteFile.get())){
    System.out.println("test");
    myFileRepository.delete(deleteFile.get());
    System.out.println("test2");
    System.out.println(deleteFile.get().getName()+" has been successful updated!");
    return deleteFile.get().getName()+" has been successful updated!";
  }else{
    return "something went wrong while deleting!";
  }
// return null;
}
public String renameFileElement(RenameFileRequest renameFileRequest) {
  System.out.println("\n\n\nrenameFileElement");
  System.out.println("-------------------------------------");
  System.out.println("groupRequest");
  System.out.println(renameFileRequest);
  Optional<FileElement> renameFileElement = fileElementRepository.findById(renameFileRequest.getId());
  renameFileElement.get().setName(renameFileRequest.getRename());
  fileElementRepository.save(renameFileElement.get());
  if(renameFileElement.get().getPath()!=null){
    String filePath = renameFileElement.get().getPath();
    Integer lastIndexOfFilePath = filePath.lastIndexOf("/");
    if ( lastIndexOfFilePath != -1) {
      String newFilePath = filePath.substring(0,  lastIndexOfFilePath + 1) + renameFileRequest.getRename()+".txt";
      System.out.println("New file path: " + newFilePath);
      File oldFile = new File(filePath);
      File newFile = new File(newFilePath);
      if (oldFile.renameTo(newFile)) {
        System.out.println("File renamed successfully.");
        return "File renamed successfully.";
      }
      else {
        System.out.println("Failed to rename the file.");
        return "Failed to rename the file.";
      }
    }
    else {
        System.out.println("Invalid file path.");
        return "Invalid file path.";
    }
  }else{
    return null;
  }
}
public String renameMemberGroup(RenameMemberGroupRequest renameMemberGroupRequest) {
  System.out.println("\n\n\nRenameMemberGroupRequest");
  System.out.println("-------------------------------------");
  System.out.println("groupRequest");
  System.out.println(renameMemberGroupRequest);
  Optional<MemberGroup> renameMemberGroup = memberGroupRepository.findById(renameMemberGroupRequest.getId());
  renameMemberGroup.get().setGroupName(renameMemberGroupRequest.getRename());
  memberGroupRepository.save(renameMemberGroup.get());
  if(!renameMemberGroup.get().getPath().isEmpty()){
    String filePath = renameMemberGroup.get().getPath();
    Integer lastIndexOfFilePath = filePath.lastIndexOf("/");
    if ( lastIndexOfFilePath != -1) {
      String newFilePath = filePath.substring(0,  lastIndexOfFilePath + 1) + renameMemberGroupRequest.getRename();
      System.out.println("New file path: " + newFilePath);
      File oldFile = new File(filePath);
      File newFile = new File(newFilePath);
      if (oldFile.renameTo(newFile)) {
        System.out.println("File renamed successfully.");
        return "Group renamed successfully.";
      }
      else {
        System.out.println("Failed to rename the file.");
        return "Failed to rename the Group.";
      }
    }
    else {
        System.out.println("Invalid file path.");
        return "Invalid file path.";
    }
  }else{
    return null;
  }
  // public void createNewFolderInUserFolder(CreateNewFolderRequest createNewFolderRequest){
  //   String owner = jwtService.extractUsername(createNewFolderRequest.getToken());
  //   Path path;
  //   if(createNewFolderRequest.getIsShared()){
  //     path = Paths.get(rootPath+owner+pathShare+createNewFolderRequest.getFolderName());
  //   }else{
  //     path = Paths.get(rootPath+owner+pathShare+createNewFolderRequest.getFolderName());
  //   }
  //   try {
  //     Files.createDirectories(path);
  //     System.out.println("Directory is created!");
  //   } catch (IOException e) {
  //     System.err.println("Failed to create directory!" + e.getMessage());
  //   }
  //   log.info("userPathHome: {}",path);
  // }
}
}





