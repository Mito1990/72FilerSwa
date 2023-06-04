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
import com.swa.filter.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.swa.filter.ObjectModel.AddFolderResponse;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {
  private final String rootPath = "../users/";
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final FolderRepository folderDirRepository;

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
//   public String createNewFile(GroupRequest groupRequest){
//       System.out.println("\n\n\ncreateNewFile");
//       System.out.println("----------------------------------------");
//       System.out.println(groupRequest);
//       String username = jwtService.extractUsername(groupRequest.getToken());
//       Optional<User> user = userRepository.findUserByUsername(username);
//       Optional<Folder> folder = folderDirRepository.findById(groupRequest.getFolderID());
//       String absolutePath;
//       String relativePath;
//       if(folder.get().getName().equalsIgnoreCase(groupRequest.getName())){
//         return "File with name "+folder.get().getName()+" exists already!";
//       }
//       if(groupRequest.isShared()){
//         Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//         absolutePath = rootPath+group.get().getAdmin()+folder.get().getPath()+"/"+groupRequest.getName()+".txt";
//         relativePath = folder.get().getPath()+"/"+groupRequest.getName()+".txt";
//       }else{
//         absolutePath = rootPath+user.get().getUsername()+folder.get().getPath()+"/"+groupRequest.getName()+".txt";
//         relativePath = folder.get().getPath()+"/"+groupRequest.getName()+".txt";
//       }
//       System.out.println(absolutePath);
//       System.out.println(relativePath);
//       try {
//         File newFile = new File(absolutePath);
//         boolean isCreated = newFile.createNewFile();
//         if (isCreated) {
//           var file = Folder.builder().name(groupRequest.getName()).parent(groupRequest.getParent()).path(relativePath).file(groupRequest.isFile()).build();
//           folderDirRepository.save(file);
//           if(groupRequest.isShared()){
//             Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//             group.get().getSharedFolders().add(file);
//             myGroupRepository.save(group.get());
//           }else{
//             homeFolder.add(file);
//             homeDirRepository.save(home);
//           }
//           System.out.println("File created successfully.");
//           return "file created";
//       } else {
//           System.out.println("File already exists or failed to create the file.");
//           return "File already exists or failed to create the file.";
//       }
//       } catch (IOException e) {
//         e.printStackTrace();
//         System.out.println("An error occurred while creating the file.");
//       }
//       System.out.println("----------------------------------------");

//     return null;
//   }
//   public AddFolderResponse newFolder(NewFolderRequest newFolderRequest) {
//     String Username = jwtService.extractUsername(newFolderRequest.getToken());
//     Optional<User> user = userRepository.findUserByUsername(Username);
//     String userRepo = user.get().getHome().getPath() + newFolderRequest.getPath();
//     Path newpath = Paths.get(userRepo);
//     List<Folder> folders = user.get().getHome().getFolders();
//     for (Folder folder : folders) {
//       if (folder.getPath().equalsIgnoreCase(newFolderRequest.getPath())) {
//         return new AddFolderResponse("Folder with name " + newFolderRequest.getName() + " exists already");
//       }
//     }if(!newFolderRequest.isShared()){
//         Folder newFolderDir = new Folder();
//         newFolderDir.setName(newFolderRequest.getName());
//         newFolderDir.setPath(newFolderRequest.getPath());
//         newFolderDir.setParent(newFolderRequest.getParent());
//         newFolderDir.setDate(new Date());
//         newFolderDir.setShared(newFolderRequest.isShared());
//         folders.add(newFolderDir);
//         folderDirRepository.save(newFolderDir);
//         userRepository.save(user.get());
//     }
  
//     try {
//       Files.createDirectories(newpath);
//       System.out.println(newpath);
//     } catch (Exception e) {
//       // TODO: handle exception
//     }
//     return new AddFolderResponse(newpath.toString());
//   }

//   public Folder getFolder(GetFolderResponse getFolderRequest) {
//     Optional<Folder> getFolder = folderDirRepository.findByName(getFolderRequest.getName());
//     if (getFolder.isPresent()) {
//       return getFolder.get();
//     } else {
//       return null;
//     }
//   }
//   // public List<String>readFile(GroupRequest groupRequest) throws IOException{
//   //   System.out.println("\n\n\nreadFile");
//   //   System.out.println("-------------------------------------");
//   //   System.out.println("groupRequest");
//   //   System.out.println(groupRequest);
//   //   String username = jwtService.extractUsername(groupRequest.getToken());
//   //   Optional<User> user = userRepository.findUserByUsername(username);
//   //   Optional<FolderDir> folder = folderDirRepository.findById(groupRequest.getFolderID());
//   //   String absolutePath = rootPath+user.get().getUsername()+folder.get().getPath();
//   //   System.out.println("absolutePath");
//   //   System.out.println(absolutePath);
//   //   Path filePath = Paths.get(absolutePath);
//   //   System.out.println("filePath");
//   //   System.out.println(filePath);
//   //   System.out.println("-------------------------------------");

//   //   return Files.readAllLines(filePath);
//   // }
//   public List<Folder> getALLFoldersUser(GetFolderRequest getFolderRequest) {
//     String Username = jwtService.extractUsername(getFolderRequest.getToken());
//     Optional<User> user = userRepository.findUserByUsername(Username);
//     HomeDir home = user.get().getHome();
//     List<Folder> folders = home.getFolders();
//     List<Folder> list = new ArrayList<>();
//     for (Folder folder : folders) {
//       if (folder.getParent() == getFolderRequest.getParentID() && folder.isShared() == false) {
//         list.add(folder);
//       }
//     }
//     System.out.println("\n\n\n------------------------------");
//     System.out.println("hello from getAllFolderUser");
//     System.out.println(list);
//     System.out.println("------------------------------\n\n\n");
//     return list;
//   }

//   public List<Folder> getListOfSharedFolderID(GetFolderRequest getFolderRequest) {
//     System.out.println("getfolderRequets: " + getFolderRequest + "\n\n\n");
//     String Username = jwtService.extractUsername(getFolderRequest.getToken());
//     Optional<User> user = userRepository.findUserByUsername(Username);
//     List<MyGroups> groups = user.get().getMygroups();
//     List<Folder> folderList = new ArrayList<>();
//     for(MyGroups group : groups){
//       List<Folder> tmpFolderList = group.getSharedFolders();
//       for(Folder folder : tmpFolderList){
//         if(folder.getParent() == getFolderRequest.getParentID())folderList.add(folder);
//       }
//     }
//     return folderList;
//   }


//   public String readFile(GroupRequest groupRequest) {
//     System.out.println("\n\n\nreadFile");
//       System.out.println("-------------------------------------");
//       System.out.println("groupRequest");
//       System.out.println(groupRequest);
//       String username = jwtService.extractUsername(groupRequest.getToken());
//       Optional<User> user = userRepository.findUserByUsername(username);
//       Optional<Folder> folder = folderDirRepository.findById(groupRequest.getFolderID());
//       String absolutePath;
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

// public void writeFile(WriteFileRequest writeFileRequest) {
//   System.out.println("\n\n\nwriteFile");
//   System.out.println("-------------------------------------");
//   System.out.println("groupRequest");
//   System.out.println(writeFileRequest);
//   String absolutePath;
//   String username = jwtService.extractUsername(writeFileRequest.getToken());
//   Optional<User> user = userRepository.findUserByUsername(username);
//   Optional<Folder> folder = folderDirRepository.findById(writeFileRequest.getFolderID());
//   if(writeFileRequest.getGroupID()==null){
//       absolutePath = rootPath+user.get().getUsername()+folder.get().getPath();
//   }else{
//     Optional<MyGroups> group = myGroupRepository.findById(writeFileRequest.getGroupID());
//     absolutePath = rootPath+group.get().getAdmin()+folder.get().getPath();
//   }

//   System.out.println("absolutePath");
//   System.out.println("filePath");
//   System.out.println("-------------------------------------");
//     try (BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath))) {
//         writer.write(writeFileRequest.getContent());
//     } catch (IOException e) {
//         e.printStackTrace();
//     }
// }

// public List<Folder> renameFile(GroupRequest groupRequest) {
//   System.out.println("hello from renameFile");
//   String username = jwtService.extractUsername(groupRequest.getToken());
//   Optional<User> user = userRepository.findUserByUsername(username);
//   String absolutePath;
//   String relativePath;
//   String rename;
//   if(groupRequest.getGroupID()==null){
//     absolutePath = rootPath+user.get().getUsername();
//     relativePath = user.get().getUsername();
//   }else{
//     Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//     absolutePath = rootPath+group.get().getAdmin();
//     relativePath = group.get().getAdmin();

//   }
//   List<Folder> update = new ArrayList<>();
//   Folder folderChange = new Folder();
//   if(groupRequest.isShared()){
//     Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//     List<Folder> folders = group.get().getSharedFolders();
//     for(Folder folder : folders){
//       if(folder.getFolder_id()==groupRequest.getFolderID()) {
//         folderChange = folder;
//         absolutePath = absolutePath+folder.getPath();
//         relativePath = folder.getPath();
//         System.out.println(absolutePath);
//       }
//       if(folder.getParent()==groupRequest.getParent()){
//         System.out.println("path");
//         String path = folder.getPath();
//         System.out.println(path);
//         System.out.println("searchString");
//         String searchString = groupRequest.getOldName();
//         System.out.println(searchString);
//         System.out.println("replacementString");
//         String replacementString = groupRequest.getName();
//         System.out.println(replacementString);
//         String newPath = path.replace(searchString, replacementString);
//         folder.setPath(newPath);
//         System.out.println(newPath);
//         update.add(folder);
//       }
//     }
//     System.out.println("first");
//     int lastIndexOfAbsoluePath = absolutePath.lastIndexOf("/");
//     System.out.println("indexoflast");
//     System.out.println(lastIndexOfAbsoluePath);
//     rename = absolutePath.substring(0, lastIndexOfAbsoluePath+1)+groupRequest.getName()+".txt";
//     System.out.println("rename");
//     System.out.println(rename);
//     int lastIndexOfRelativePath = relativePath.lastIndexOf("/");
//     relativePath = relativePath.substring(0, lastIndexOfRelativePath+1)+groupRequest.getName()+".txt";
//     System.out.println("relativPath");
//     System.out.println(relativePath);
//     File file = new File(absolutePath);
//     boolean success = file.renameTo(new File(rename));
//     if (success) {
//         System.out.println("File or folder renamed successfully.");
//     } else {
//         System.out.println("Failed to rename the file or folder.");
//     }
//     folderChange.setPath(relativePath);
//     folderChange.setName(groupRequest.getName());
//     folderDirRepository.save(folderChange);
//     myGroupRepository.save(group.get());
//   }else{
//     HomeDir home = user.get().getHome();
//     List<Folder> folders = home.getFolders();
//     for(Folder folder : folders){
//       if(folder.getFolder_id()==groupRequest.getFolderID()) {
//         absolutePath = absolutePath+folder.getPath();
//         relativePath = folder.getPath();
//         folderChange = folder;
//         System.out.println("hello from home");
//         System.out.println(absolutePath);
//       }
//       if(folder.getParent()==groupRequest.getParent()){
//         System.out.println("path");
//         String path = folder.getPath();
//         System.out.println(path);
//         System.out.println("searchString");
//         String searchString = groupRequest.getOldName();
//         System.out.println(searchString);
//         System.out.println("replacementString");
//         String replacementString = groupRequest.getName();
//         System.out.println(replacementString);
//         String newPath = path.replace(searchString, replacementString);
//         folder.setPath(newPath);
//         System.out.println(newPath);
//         update.add(folder);
//       }
//     }
//     System.out.println(update);
//     System.out.println("first");
//     int lastIndexOfAbsoluePath = absolutePath.lastIndexOf("/");
//     System.out.println(lastIndexOfAbsoluePath);
//     rename = absolutePath.substring(0, lastIndexOfAbsoluePath+1)+groupRequest.getName()+".txt";
//     System.out.println("rename");
//     System.out.println(rename);
//     int lastIndexOfRelativePath = relativePath.lastIndexOf("/");
//     relativePath = relativePath.substring(0, lastIndexOfRelativePath+1)+groupRequest.getName()+".txt";
//     System.out.println("relativPath");
//     System.out.println(relativePath);
//     if(!groupRequest.isFile()){
//       int cut = rename.lastIndexOf(".");
//       rename = rename.substring(0, cut+1);
//       cut = relativePath.lastIndexOf(".");
//       relativePath =  relativePath.substring(0, cut);
//     }
//     File file = new File(absolutePath);
//     boolean success = file.renameTo(new File(rename));
//     if (success) {
//         System.out.println("File or folder renamed successfully.");
//     } else {
//         System.out.println("Failed to rename the file or folder.");
//     }
//     folderChange.setPath(relativePath);
//     folderChange.setName(groupRequest.getName());
//     folderDirRepository.save(folderChange);
//     folderDirRepository.saveAll(update);
//     homeDirRepository.save(home);
//   }
//   return update;
// }

// public List<Folder> deleteFile(GroupRequest groupRequest){
//   System.out.println("\n\n\nDeleteFile");
//   System.out.println("-------------------------------------");
//   System.out.println("groupRequest");
//   System.out.println(groupRequest);
//   String username = jwtService.extractUsername(groupRequest.getToken());
//   Optional<User> user = userRepository.findUserByUsername(username);
//   String absolutePath;
//   // String absolutePath= rootPath+user.get().getUsername();
//   if(groupRequest.getGroupID()==null){
//     absolutePath = rootPath+user.get().getUsername();
//   }else{
//     Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//     absolutePath = rootPath+group.get().getAdmin();
//   }
//   List<Folder> update = new ArrayList<>();
//   Folder folderToDelete = new Folder();
//   if(groupRequest.isShared()){
//     Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
//     System.out.println("\n\n\n-------------------------------------");
//     System.out.println("getSharedFolders Before delete!");
//     System.out.println(group.get().getSharedFolders());
//     System.out.println("-------------------------------------\n\n\n");
//     List<Folder> folders = group.get().getSharedFolders();
//     for(Folder folder : folders){
//       if(folder.getFolder_id()==groupRequest.getFolderID()) {
//         absolutePath = absolutePath+folder.getPath();
//         System.out.println(absolutePath);
//         System.out.println("delete worked!\n");
//         folderToDelete = folder;
//       }else if(folder.getParent()==groupRequest.getParent()){
//         update.add(folder);
//       }
//     }
//     System.out.println("\n\n\n-------------------------------------");
//     System.out.println("getSharedFolders after delete!");
//     System.out.println(group.get().getSharedFolders());
//     System.out.println("-------------------------------------\n\n\n");
//     folders.remove(folderToDelete);
//     myGroupRepository.save(group.get());
//   }else{
//     HomeDir home = user.get().getHome();
//     List<Folder> folders = home.getFolders();
//     for(Folder folder : folders){
//       if(folder.getFolder_id()==groupRequest.getFolderID()) {
//         absolutePath = absolutePath+folder.getPath();
//         System.out.println(absolutePath);
//         System.out.println("delete worked!\n");
//         folderToDelete = folder;
//       }else if(folder.getParent()==groupRequest.getParent()){
//         update.add(folder);
//       }
//     }
//     folders.remove(folderToDelete);
//   }
//   folderDirRepository.delete(folderToDelete);

//   System.out.println("\n\n\n-------------------------------------");
//   System.out.println("absolutePath");
//   System.out.println(absolutePath);
//   System.out.println("-------------------------------------\n\n\n");

//   System.out.println("\n\n\n-------------------------------------");
//   System.out.println("Update");
//   System.out.println(update);
//   System.out.println("-------------------------------------\n\n\n");
//   File file = new File(absolutePath);

//   if (file.exists()) {
//       boolean deleted = file.delete();
//       if (deleted) {
//           System.out.println("File deleted successfully.");
//       } else {
//           System.out.println("Failed to delete the file.");
//       }
//   } else {
//       System.out.println("File does not exist.");
//   }
//   return update;
// }

}



