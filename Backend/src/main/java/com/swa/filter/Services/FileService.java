package com.swa.filter.Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Service
public class FileService {
    public void createPathAndFile(String username, Long id){
        try {
            Path path = Paths.get("../UserWorkSpace/"+username);
            try {
                File file = new File("../UserWorkSpace/"+username+"/"+username+".txt");
                if (file.createNewFile()) {
                  System.out.println("File created: " + file.getName());
                } else {
                  System.out.println("File already exists.");
                }
              } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
              }
            
            Files.createDirectories(path);
            System.out.println("Directory is created!");
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
        writeToFile(username,id);
    }
    public void writeToFile(String username,Long id) {
        try {
          FileWriter myWriter = new FileWriter("../UserWorkSpace/"+username+"/"+username+".txt");
          myWriter.write("UserID:"+id);
          myWriter.close();
          System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
      }

}