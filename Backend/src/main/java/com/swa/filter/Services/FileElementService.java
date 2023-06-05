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
import com.swa.filter.mySQLTables.User;
import jakarta.transaction.Transactional;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GetFolderResponse;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.ObjectModel.WriteFileRequest;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.MemberGroupRepository;
import com.swa.filter.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.swa.filter.ObjectModel.CreateNewFolderRequest;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FileElementService {
  private final String rootPath = "../users/";
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final FolderRepository folderRepository;
  private final MemberGroupRepository memberGroupRepository;

  public void createUserFolder(String username) {
    Path userPathHome = Paths.get(rootPath+username+"/home");
    Path userPathShare = Paths.get(rootPath+username+"/share");
    try {
      Files.createDirectories(userPathHome);
      Files.createDirectories(userPathShare);
      System.out.println("Directory is created!");
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }
    log.info("userPathHome: {}",userPathHome);
    log.info("userPathShare: {}",userPathShare);
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
    String owner = jwtService.extractUsername(createNewFolderRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(owner);
    Optional<Folder> parentFolder = folderRepository.findById(createNewFolderRequest.getParentFolderID());
    Folder newFolder = new Folder(createNewFolderRequest.getFolderName(),createNewFolderRequest.getParentFolderID(),createNewFolderRequest.getIsShared());
    parentFolder.get().getChildren().add(newFolder);
    folderRepository.save(newFolder);
    // userRepository.save(user.get());
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

}



