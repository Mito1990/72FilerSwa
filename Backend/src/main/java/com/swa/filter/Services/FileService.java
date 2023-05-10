package com.swa.filter.Services;

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
import com.swa.filter.mySQLTables.User;

import jakarta.transaction.Transactional;

import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GetFolderResponse;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.Repository.FolderDirRepository;
import com.swa.filter.Repository.HomeDirRepository;
import com.swa.filter.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
@Transactional
@RequiredArgsConstructor
@Service
public class FileService {
    private final String rootPath = "../UserWorkSpace/";
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FolderDirRepository folderDirRepository;
    public String createUserFolder(String path){
          Path userpath = Paths.get(rootPath+path+"/home/");
          System.out.println(userpath.toString());
          try {
            Files.createDirectories(userpath);
            System.out.println("Directory is created!");
          } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
          }
          return userpath.toString();
    }

    public String newFolder(NewFolderRequest newFolderRequest){
      String Username = jwtService.extractUsername(newFolderRequest.getToken());
      Optional<User> user = userRepository.findUserByUsername(Username);
      String userRepo = user.get().getHome().getPath()+"/"+newFolderRequest.getPath();
      Path newpath  = Paths.get(userRepo);
      List<FolderDir> folders = user.get().getHome().getFolders();
      for(FolderDir folder : folders){
        if(folder.getPath().equalsIgnoreCase(newFolderRequest.getPath())){
          System.out.println("hello");
          return "Folder with name "+newFolderRequest.getName()+" exists already";
        }
      }
      FolderDir newfolderDir = new FolderDir();
      newfolderDir.setName(newFolderRequest.getName());
      newfolderDir.setPath(newFolderRequest.getPath());
      newfolderDir.setParent(newpath.toString());
      newfolderDir.setDate(new Date());
      folders.add(newfolderDir);
      folderDirRepository.save(newfolderDir);
      userRepository.save(user.get());
      try {
        Files.createDirectories(newpath);
        System.out.println(newpath); 
      } catch (Exception e) {
        // TODO: handle exception
      }
      return newpath.toString();
    }


    // public void n(String folder){
    //   try {
    //     File file = new File("../UserWorkSpace/"+username+"/"+username);
    //     if (file.createNewFile()) {
    //       System.out.println("File created: " + file.getName());
    //     } else {
    //       System.out.println("File already exists.");
    //     }
    //   } catch (IOException e) {
    //     System.out.println("An error occurred.");
    //     e.printStackTrace();
    //   }
    // }
    public FolderDir getFolder(GetFolderResponse getFolderRequest){
      Optional<FolderDir> getFolder = folderDirRepository.findByName(getFolderRequest.getName());
      if(getFolder.isPresent()){
        return getFolder.get();
      }else{
        return null;
      }
    }
    public List<FolderDir>getALLFoldersUser(GetFolderRequest getFolderRequest){
      String Username = jwtService.extractUsername(getFolderRequest.getToken());
      Optional<User> user = userRepository.findUserByUsername(Username);
      List<FolderDir>folders = user.get().getHome().getFolders();
      return folders;
    }


    // public void writeToFile(String username,Long id) {
    //     try {
    //       FileWriter myWriter = new FileWriter("../UserWorkSpace/"+username+"/"+username+".txt");
    //       myWriter.write("UserID:"+id);
    //       myWriter.close();
    //       System.out.println("Successfully wrote to the file.");
    //     } catch (IOException e) {
    //       System.out.println("An error occurred.");
    //       e.printStackTrace();
    //     }
    //   }

}