package com.swa.filter.Services;

import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserService  {
    private final UserRepository userRepository;
    
    public Optional<User> getUser(String username) {
        log.info("Fetching user:{} from Database",username);
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users from Database");
        return userRepository.findAll();
    }

    public List<String>listOfUsernames(){
        List<User> listOfAllUsers = userRepository.findAll();
        List<String> listOfUsernames = new ArrayList<>();
        listOfAllUsers.forEach(e ->{
            listOfUsernames.add(e.getUsername());
        });
        return listOfUsernames;
    }
    
    public boolean checkIfUserExists(String username) {
        List<String>userList = listOfUsernames();
        for (String user : userList) {
            if(user.equalsIgnoreCase(username))return true;
        }
        return false;
    }
}
