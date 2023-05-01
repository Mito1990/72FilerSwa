package com.swa.filter.RestController;

import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/")
public class UserController {
  private final UserService userService;

  @GetMapping(path = "/users/get/all" )
  public ResponseEntity<List<User>>getAllUsers(){
    return ResponseEntity.ok().body(userService.getAllUsers());
  }
  @GetMapping(path = "/users/get/user" )
  public ResponseEntity<?>getUsers(String userName){
    if(!userService.checkIfUserExists(userName))return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User with username:{"+userName+"} doesn't exists!");
    else return ResponseEntity.ok().body(userService.getUser(userName));
  }

  @PostMapping(path = "/users/add/user")
  public ResponseEntity<?> addUser(@RequestBody User user) {
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/add/user").toUriString());
    if(user.getName().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field name is empty!");
    else if(user.getUserName().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field username is empty!");
    else if(user.getPassword().isEmpty())return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Field password is empty!");
    else if(user.getPassword().length()<8)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password is to short enter at least 8 character");
    else if(userService.getUser(user.getUserName())!=null)return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User with username:{"+user.getUserName()+"} exists already in table:{"+userService.getUser(user.getUserName())+"}");
    else if(ResponseEntity.created(uri).body(userService.addUser(user))==null);
    return ResponseEntity.created(uri).body(userService.addUser(user));
  }
}


