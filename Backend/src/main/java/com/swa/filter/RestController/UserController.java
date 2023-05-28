package com.swa.filter.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {
  private final UserService userService;
  @GetMapping(path = "/get/all" )
  public ResponseEntity<List<User>>getAllUsers(){
    return ResponseEntity.ok().body(userService.getAllUsers());
  }
  @GetMapping(path = "/get/user" )
  public ResponseEntity<?>getUsers(@RequestParam String username){
    if(!userService.checkIfUserExists(username))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User with username:{"+username+"} doesn't exists!");
    else return ResponseEntity.ok().body(userService.getUser(username));
  }
  @GetMapping(path = "/get/users" )
  public ResponseEntity<?>getListOfUsers() throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    // Convert the object to JSON string
    String jsonString = objectMapper.writeValueAsString(userService.listOfUsernames());
    return ResponseEntity.ok().body(jsonString);
  }

}


