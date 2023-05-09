// package com.swa.filter.Services;

// import java.io.File;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// import org.springframework.stereotype.Service;

// import com.swa.filter.mySQLTables.HomeDir;
// import com.swa.filter.Repository.HomeDirRepository;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// @Data
// @AllArgsConstructor
// @Service
// public class FileService {
//     private final HomeDirRepository homeDirRepository;
//     String rootPath = "../UserWorkSpace/";
//     public void createUserFolder(String username){
//         try {
//             Path path = Paths.get(rootPath+username);
//             Files.createDirectories(path);
//             System.out.println("Directory is created!");

//           } catch (IOException e) {
//             System.err.println("Failed to create directory!" + e.getMessage());
//           }
//     }
//     public void newFolder(String folder, String path){
//         // HomeDir homeDir = homeDirRepository.findByName(folder);
//         try {
//           Path newpath  = Paths.get(path+folder);
//           Files.createDirectories(newpath);
//           // homeDir.setPath(newpath);
//         } catch (Exception e) {
//           // TODO: handle exception
//         }
//     }
//     // public void n(String folder){
//     //   try {
//     //     File file = new File("../UserWorkSpace/"+username+"/"+username);
//     //     if (file.createNewFile()) {
//     //       System.out.println("File created: " + file.getName());
//     //     } else {
//     //       System.out.println("File already exists.");
//     //     }
//     //   } catch (IOException e) {
//     //     System.out.println("An error occurred.");
//     //     e.printStackTrace();
//     //   }
//     // }
  


//     public void writeToFile(String username,Long id) {
//         try {
//           FileWriter myWriter = new FileWriter("../UserWorkSpace/"+username+"/"+username+".txt");
//           myWriter.write("UserID:"+id);
//           myWriter.close();
//           System.out.println("Successfully wrote to the file.");
//         } catch (IOException e) {
//           System.out.println("An error occurred.");
//           e.printStackTrace();
//         }
//       }

// }