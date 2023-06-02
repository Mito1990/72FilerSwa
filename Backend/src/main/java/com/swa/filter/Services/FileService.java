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
import com.swa.filter.mySQLTables.FolderDir;
import com.swa.filter.mySQLTables.HomeDir;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;
import jakarta.transaction.Transactional;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GetFolderResponse;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.ObjectModel.WriteFileRequest;
import com.swa.filter.Repository.FolderDirRepository;
import com.swa.filter.Repository.HomeDirRepository;
import com.swa.filter.Repository.MyGroupRepository;
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
  private final MyGroupRepository myGroupRepository;
  private final HomeDirRepository homeDirRepository;

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
  public String createNewFile(GroupRequest groupRequest){
      System.out.println("\n\n\ncreateNewFile");
      System.out.println("----------------------------------------");
      System.out.println(groupRequest);
      String username = jwtService.extractUsername(groupRequest.getToken());
      Optional<User> user = userRepository.findUserByUsername(username);
      HomeDir home = user.get().getHome();
      List<FolderDir> homeFolder = home.getFolders();
      Optional<FolderDir> folder = folderDirRepository.findById(groupRequest.getFolderID());
      String absolutePath;
      String relativePath;
      if(folder.get().getName().equalsIgnoreCase(groupRequest.getName())){
        return "File with name "+folder.get().getName()+" exists already!";
      }
      if(groupRequest.isShared()){
        Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
        absolutePath = rootPath+group.get().getAdmin()+folder.get().getPath()+"/"+groupRequest.getName()+".txt";
        relativePath = folder.get().getPath()+"/"+groupRequest.getName()+".txt";
      }else{
        absolutePath = rootPath+user.get().getUsername()+folder.get().getPath()+"/"+groupRequest.getName()+".txt";
        relativePath = folder.get().getPath()+"/"+groupRequest.getName()+".txt";
      }
      System.out.println(absolutePath);
      System.out.println(relativePath);
      try {
        File newFile = new File(absolutePath);
        boolean isCreated = newFile.createNewFile();
        if (isCreated) {
          var file = FolderDir.builder().name(groupRequest.getName()).parent(groupRequest.getParent()).path(relativePath).file(groupRequest.isFile()).build();
          folderDirRepository.save(file);
          if(groupRequest.isShared()){
            Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
            group.get().getSharedFolders().add(file);
            myGroupRepository.save(group.get());
          }else{
            homeFolder.add(file);
            homeDirRepository.save(home);
          }
          System.out.println("File created successfully.");
          return "file created";
      } else {
          System.out.println("File already exists or failed to create the file.");
          return "File already exists or failed to create the file.";
      }
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("An error occurred while creating the file.");
      }
      System.out.println("----------------------------------------");

    return null;
  }
  public AddFolderResponse newFolder(NewFolderRequest newFolderRequest) {
    String Username = jwtService.extractUsername(newFolderRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(Username);
    String userRepo = user.get().getHome().getPath() + newFolderRequest.getPath();
    Path newpath = Paths.get(userRepo);
    List<FolderDir> folders = user.get().getHome().getFolders();
    for (FolderDir folder : folders) {
      if (folder.getPath().equalsIgnoreCase(newFolderRequest.getPath())) {
        return new AddFolderResponse("Folder with name " + newFolderRequest.getName() + " exists already");
      }
    }if(!newFolderRequest.isShared()){
        FolderDir newFolderDir = new FolderDir();
        newFolderDir.setName(newFolderRequest.getName());
        newFolderDir.setPath(newFolderRequest.getPath());
        newFolderDir.setParent(newFolderRequest.getParent());
        newFolderDir.setDate(new Date());
        newFolderDir.setShared(newFolderRequest.isShared());
        folders.add(newFolderDir);
        folderDirRepository.save(newFolderDir);
        userRepository.save(user.get());
    }
  
    try {
      Files.createDirectories(newpath);
      System.out.println(newpath);
    } catch (Exception e) {
      // TODO: handle exception
    }
    return new AddFolderResponse(newpath.toString());
  }

  public FolderDir getFolder(GetFolderResponse getFolderRequest) {
    Optional<FolderDir> getFolder = folderDirRepository.findByName(getFolderRequest.getName());
    if (getFolder.isPresent()) {
      return getFolder.get();
    } else {
      return null;
    }
  }
  // public List<String>readFile(GroupRequest groupRequest) throws IOException{
  //   System.out.println("\n\n\nreadFile");
  //   System.out.println("-------------------------------------");
  //   System.out.println("groupRequest");
  //   System.out.println(groupRequest);
  //   String username = jwtService.extractUsername(groupRequest.getToken());
  //   Optional<User> user = userRepository.findUserByUsername(username);
  //   Optional<FolderDir> folder = folderDirRepository.findById(groupRequest.getFolderID());
  //   String absolutePath = rootPath+user.get().getUsername()+folder.get().getPath();
  //   System.out.println("absolutePath");
  //   System.out.println(absolutePath);
  //   Path filePath = Paths.get(absolutePath);
  //   System.out.println("filePath");
  //   System.out.println(filePath);
  //   System.out.println("-------------------------------------");

  //   return Files.readAllLines(filePath);
  // }
  public List<FolderDir> getALLFoldersUser(GetFolderRequest getFolderRequest) {
    String Username = jwtService.extractUsername(getFolderRequest.getToken());
    Optional<User> user = userRepository.findUserByUsername(Username);
    HomeDir home = user.get().getHome();
    List<FolderDir> folders = home.getFolders();
    List<FolderDir> list = new ArrayList<>();
    for (FolderDir folder : folders) {
      if (folder.getParent() == getFolderRequest.getParentID() && folder.isShared() == false) {
        list.add(folder);
      }
    }
    System.out.println("\n\n\n------------------------------");
    System.out.println("hello from getAllFolderUser");
    System.out.println(list);
    System.out.println("------------------------------\n\n\n");
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


  public String readFile(GroupRequest groupRequest) {
    System.out.println("\n\n\nreadFile");
      System.out.println("-------------------------------------");
      System.out.println("groupRequest");
      System.out.println(groupRequest);
      String username = jwtService.extractUsername(groupRequest.getToken());
      Optional<User> user = userRepository.findUserByUsername(username);
      Optional<FolderDir> folder = folderDirRepository.findById(groupRequest.getFolderID());
      String absolutePath;
      if(groupRequest.getGroupID()==null){
        absolutePath = rootPath+user.get().getUsername()+folder.get().getPath();
      }else{
        Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
        absolutePath = rootPath+group.get().getAdmin()+folder.get().getPath();
      }
      System.out.println("absolutePath");
      System.out.println(absolutePath);
      System.out.println("filePath");
      System.out.println("-------------------------------------");
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return content.toString();
}

public void writeFile(WriteFileRequest writeFileRequest) {
  System.out.println("\n\n\nwriteFile");
  System.out.println("-------------------------------------");
  System.out.println("groupRequest");
  System.out.println(writeFileRequest);
  String absolutePath;
  String username = jwtService.extractUsername(writeFileRequest.getToken());
  Optional<User> user = userRepository.findUserByUsername(username);
  Optional<FolderDir> folder = folderDirRepository.findById(writeFileRequest.getFolderID());
  if(writeFileRequest.getGroupID()==null){
      absolutePath = rootPath+user.get().getUsername()+folder.get().getPath();
  }else{
    Optional<MyGroups> group = myGroupRepository.findById(writeFileRequest.getGroupID());
    absolutePath = rootPath+group.get().getAdmin()+folder.get().getPath();
  }

  System.out.println("absolutePath");
  System.out.println("filePath");
  System.out.println("-------------------------------------");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath))) {
        writer.write(writeFileRequest.getContent());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public List<FolderDir> deleteFile(GroupRequest groupRequest){
  System.out.println("\n\n\nDeleteFile");
  System.out.println("-------------------------------------");
  System.out.println("groupRequest");
  System.out.println(groupRequest);
  String username = jwtService.extractUsername(groupRequest.getToken());
  Optional<User> user = userRepository.findUserByUsername(username);
  String absolutePath;
  // String absolutePath= rootPath+user.get().getUsername();
  if(groupRequest.getGroupID()==null){
    absolutePath = rootPath+user.get().getUsername();
  }else{
    Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
    absolutePath = rootPath+group.get().getAdmin();
  }
  List<FolderDir> update = new ArrayList<>();
  FolderDir folderToDelete = new FolderDir();
  if(groupRequest.isShared()){
    Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
    System.out.println("\n\n\n-------------------------------------");
    System.out.println("getSharedFolders Before delete!");
    System.out.println(group.get().getSharedFolders());
    System.out.println("-------------------------------------\n\n\n");
    List<FolderDir> folders = group.get().getSharedFolders();
    for(FolderDir folder : folders){
      if(folder.getFolder_id()==groupRequest.getFolderID()) {
        absolutePath = absolutePath+folder.getPath();
        System.out.println(absolutePath);
        System.out.println("delete worked!\n");
        folderToDelete = folder;
      }else if(folder.getParent()==groupRequest.getParent()){
        update.add(folder);
      }
    }
    System.out.println("\n\n\n-------------------------------------");
    System.out.println("getSharedFolders after delete!");
    System.out.println(group.get().getSharedFolders());
    System.out.println("-------------------------------------\n\n\n");
    folders.remove(folderToDelete);
    myGroupRepository.save(group.get());
  }else{
    HomeDir home = user.get().getHome();
    List<FolderDir> folders = home.getFolders();
    for(FolderDir folder : folders){
      if(folder.getFolder_id()==groupRequest.getFolderID()) {
        absolutePath = absolutePath+folder.getPath();
        System.out.println(absolutePath);
        System.out.println("delete worked!\n");
        folderToDelete = folder;
      }else if(folder.getParent()==groupRequest.getParent()){
        update.add(folder);
      }
    }
    folders.remove(folderToDelete);
  }
  folderDirRepository.delete(folderToDelete);

  System.out.println("\n\n\n-------------------------------------");
  System.out.println("absolutePath");
  System.out.println(absolutePath);
  System.out.println("-------------------------------------\n\n\n");

  System.out.println("\n\n\n-------------------------------------");
  System.out.println("Update");
  System.out.println(update);
  System.out.println("-------------------------------------\n\n\n");
  File file = new File(absolutePath);

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
  return update;
}

}



