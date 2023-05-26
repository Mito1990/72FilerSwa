package com.swa.filter.Services;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.swa.filter.mySQLTables.FolderDir;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;
import jakarta.transaction.Transactional;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GetFolderResponse;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.Repository.FolderDirRepository;
import com.swa.filter.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.swa.filter.ObjectModel.AddFolderResponse;

@Transactional
@RequiredArgsConstructor
@Service
public class FileService {
  private final String rootPath = "../UserWorkSpace/";
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final FolderDirRepository folderDirRepository;

  public String createUserFolder(String path) {
    Path userpath = Paths.get(rootPath + path);

    System.out.println(userpath.toString());
    try {
      Files.createDirectories(userpath);
      System.out.println("Directory is created!");
    } catch (IOException e) {
      System.err.println("Failed to create directory!" + e.getMessage());
    }
    return userpath.toString();
  }

  public AddFolderResponse newFolder(NewFolderRequest newFolderRequest) {
    String Username = jwtService.extractUsername(newFolderRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(Username);
    String userRepo = user.get().getHome().getPath() + newFolderRequest.getPath();
    System.out.println(userRepo + "\n\n\n");
    Path newpath = Paths.get(userRepo);
    List<FolderDir> folders = user.get().getHome().getFolders();
    for (FolderDir folder : folders) {
      if (folder.getPath().equalsIgnoreCase(newFolderRequest.getPath())) {
        return new AddFolderResponse("Folder with name " + newFolderRequest.getName() + " exists already");
      }
    }
    FolderDir newfolderDir = new FolderDir();
    newfolderDir.setName(newFolderRequest.getName());
    newfolderDir.setPath(newFolderRequest.getPath());
    newfolderDir.setParent(newFolderRequest.getParent());
    newfolderDir.setDate(new Date());
    newfolderDir.setShared(newFolderRequest.isShared());
    folders.add(newfolderDir);
    folderDirRepository.save(newfolderDir);
    userRepository.save(user.get());
    try {
      Files.createDirectories(newpath);
      System.out.println(newpath);
    } catch (Exception e) {
      // TODO: handle exception
    }
    return new AddFolderResponse(newpath.toString());
  }
  public AddFolderResponse newFolderGroup(NewFolderGroupRequest newFolderGroupRequest) {
    String Username = jwtService.extractUsername(newFolderGroupRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(Username);
    MyGroups group = user.get().getMygroups().get(newFolderGroupRequest.getGroupID());
    String userRepo = user.get().getHome().getPath() + newFolderGroupRequest.getPath();
    // System.out.println(userRepo + "\n\n\n");
    Path newpath = Paths.get(userRepo);
    List<FolderDir> folders = group.getSharedFolders();
    for (FolderDir folder : folders) {
      if (folder.getPath().equalsIgnoreCase(newFolderGroupRequest.getPath())) {
        return new AddFolderResponse("Folder with name " + newFolderGroupRequest.getName() + " exists already");
      }
    }
    FolderDir newfolderDir = new FolderDir();
    newfolderDir.setName(newFolderGroupRequest.getName());
    newfolderDir.setPath(newFolderGroupRequest.getPath());
    newfolderDir.setParent(newFolderGroupRequest.getParent());
    newfolderDir.setDate(new Date());
    newfolderDir.setShared(newFolderGroupRequest.isShared());
    folders.add(newfolderDir);
    folderDirRepository.save(newfolderDir);
    userRepository.save(user.get());
    try {
      Files.createDirectories(newpath);
      System.out.println(newpath);
    } catch (Exception e) {
      // TODO: handle exception
    }
    return new AddFolderResponse(newpath.toString());
  }
  // public void n(String folder){
  // try {
  // File file = new File("../UserWorkSpace/"+username+"/"+username);
  // if (file.createNewFile()) {
  // System.out.println("File created: " + file.getName());
  // } else {
  // System.out.println("File already exists.");
  // }
  // } catch (IOException e) {
  // System.out.println("An error occurred.");
  // e.printStackTrace();
  // }
  // }
  public FolderDir getFolder(GetFolderResponse getFolderRequest) {
    Optional<FolderDir> getFolder = folderDirRepository.findByName(getFolderRequest.getName());
    if (getFolder.isPresent()) {
      return getFolder.get();
    } else {
      return null;
    }
  }

  public List<FolderDir> getALLFoldersUser(GetFolderRequest getFolderRequest) {
    String Username = jwtService.extractUsername(getFolderRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(Username);
    List<FolderDir> folders = user.get().getHome().getFolders();
    List<FolderDir> list = new ArrayList<>();
    for (FolderDir folder : folders) {
      if (folder.getParent() == getFolderRequest.getParentID() && folder.isShared() == false) {
        list.add(folder);
      }
    }
    return list;
  }

  public List<FolderDir> getListOfSharedFolderID(GetFolderRequest getFolderRequest) {
    System.out.println("getfolderRequets: " + getFolderRequest + "\n\n\n");
    String Username = jwtService.extractUsername(getFolderRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(Username);
    List<MyGroups> groups = user.get().getMygroups();
    List<FolderDir> folderList = new ArrayList<>();
    for(MyGroups group : groups){
      List<FolderDir> tmpFolderList = group.getSharedFolders();
      for(FolderDir folder : tmpFolderList){
        if(folder.getParent() == getFolderRequest.getParentID())folderList.add(folder);
      }
    }
    return folderList;
  }
}
  // public void writeToFile(String username,Long id) {
  // try {
  // FileWriter myWriter = new
  // FileWriter("../UserWorkSpace/"+username+"/"+username+".txt");
  // myWriter.write("UserID:"+id);
  // myWriter.close();
  // System.out.println("Successfully wrote to the file.");
  // } catch (IOException e) {
  // System.out.println("An error occurred.");
  // e.printStackTrace();
  // }
  // }

