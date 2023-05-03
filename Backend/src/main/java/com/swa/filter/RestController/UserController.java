package com.swa.filter.RestController;

import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
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

}


