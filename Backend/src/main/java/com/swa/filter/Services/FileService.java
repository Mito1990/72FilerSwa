package com.swa.filter.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.swa.filter.mySQLTables.FolderDir;
// import com.swa.filter.mySQLTables.User;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.Repository.FolderDirRepository;
// import com.swa.filter.Repository.HomeDirRepository;
import com.swa.filter.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {
    // private final HomeDirRepository homeDirRepository;
    // private final String rootPath = "../UserWorkSpace/";
    // private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FolderDirRepository folderDirRepository;
    public void createUserFolder(Long long1,String path){
        try {
            Path userpath = Paths.get(path+"/home");
            Files.createDirectories(userpath);
            System.out.println("Directory is created!");

          } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
          }
    }
    public String newFolder(NewFolderRequest newFolderRequest){
      String Username = jwtService.extractUsername(newFolderRequest.getToken());
      // Optional<User> user = userRepository.findUserByUsername(Username);
      // List<FolderDir> folders = user.get().getHome().getFolder();
      // for(FolderDir folder : folders){
      //   if(folder.getName().equalsIgnoreCase(newFolderRequest.getName())){
      //     return "Folder with name "+newFolderRequest.getName()+" exists already";
      //   }
      // }
      FolderDir folderDir = new FolderDir();
      folderDir.setName(newFolderRequest.getName());
      folderDir.setPath(newFolderRequest.getPath()+"/"+newFolderRequest.getName());
      folderDir.setDate(new Date());
      folderDirRepository.save(folderDir);
      try {
        Path newpath  = Paths.get(newFolderRequest.getPath()+"/"+newFolderRequest.getName());
        Files.createDirectories(newpath);
      } catch (Exception e) {
        // TODO: handle exception
      }
      return newFolderRequest.getPath()+"/"+newFolderRequest.getName();
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
    public FolderDir getFolder(GetFolderRequest getFolderRequest){
      Optional<FolderDir> getFolder = folderDirRepository.findByName(getFolderRequest.getName());
      if(getFolder.isPresent()){
        return getFolder.get();
      }else{
        return null;
      }
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