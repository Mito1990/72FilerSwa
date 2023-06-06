package com.swa.filter.Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
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
import com.swa.filter.ObjectModel.WriteFileRequest;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.MemberGroupRepository;
import com.swa.filter.Repository.MyFileRepository;
import com.swa.filter.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.swa.filter.ObjectModel.CreateNewFileRequest;
import com.swa.filter.ObjectModel.CreateNewFolderRequest;

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
  public void createMemberGroupFolder(String groupName,Integer memberGroupID) {
    Path groupPath = Paths.get(rootGroups+memberGroupID.toString()+"_"+groupName);
    try {
      Files.createDirectories(groupPath);
      System.out.println("Directory is created!");
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }
    log.info("create member groupfolder: {}",groupPath);
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
    parentFolder.get().getChildren().add(newFile);
    folderRepository.save(parentFolder.get());
    createNewFileInUserFolder(createNewFileRequest,newFile.getId());
    System.out.println("----------------------------------------------\n\n\n");
    return parentFolder.get();
  }

  public void createNewFileInUserFolder(CreateNewFileRequest createNewFileRequest,Integer fileID){
    String filePath;
    String owner = jwtService.extractUsername(createNewFileRequest.getToken());
    if(createNewFileRequest.getIsShared()){
      filePath = rootGroups+createNewFileRequest.getGroupID().toString()+"_"+createNewFileRequest.getGroupName()+"/"+fileID.toString()+"_"+createNewFileRequest.getFileName()+".txt";
    }else{
      filePath = rootUsers+owner+pathHome+fileID.toString()+"_"+createNewFileRequest.getFileName()+".txt";
    }
    // Create a File object
    File file = new File(filePath);
    try {
        // Create a new file
        boolean isNewFileCreated = file.createNewFile();
        if (isNewFileCreated) {
            System.out.println("File created successfully.");
        } else {
            System.out.println("File already exists.");
        }
    } catch (IOException e) {
        System.out.println("An error occurred while creating the file: " + e.getMessage());
    }

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

// public String readFile(ReadFileRequest readFileRequest) {
//     System.out.println("\n\n\nreadFile");
//       System.out.println("-------------------------------------");
//       System.out.println("readRequest");
//       System.out.println(readFileRequest);
//       Optional<MyFile> readFile = myFileRepository.findById(readFileRequest.getFileID());
//       String absolutePath;
//       String filePath = rootPath+
//       if(groupRequest.getGroupID()==null){
//         absolutePath = rootPath+user.get().getUsername()+folder.get().getPath();
//       }else{
//         Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//         absolutePath = rootPath+group.get().getAdmin()+folder.get().getPath();
//       }
//       System.out.println("absolutePath");
//       System.out.println(absolutePath);
//       System.out.println("filePath");
//       System.out.println("-------------------------------------");
//     StringBuilder content = new StringBuilder();
//     try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
//         String line;
//         while ((line = reader.readLine()) != null) {
//             content.append(line).append("\n");
//         }
//     } catch (IOException e) {
//         e.printStackTrace();
//     }
//     return content.toString();
// }


}




