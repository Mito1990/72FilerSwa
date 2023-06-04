package com.swa.filter.Services;

import com.swa.filter.ObjectModel.ListOfUsernameRequest;
import com.swa.filter.Repository.MemberRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.MemberGroup;
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
    private final JwtService jwtService;
    public Optional<User> getUser(String username) {
        log.info("Fetching user:{} from Database",username);
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users from Database");
        return userRepository.findAll();
    }

    public List<String>listOfAllUsernames(){
        List<User>users = getAllUsers();
        List<String>listOfAllUsernames = new ArrayList<>();
        for(User user : users){
            listOfAllUsernames.add(user.getUsername());
        }
        log.info("Fetching all listOfAllUsernames from Database: ",listOfAllUsernames);
        return listOfAllUsernames;
    }
    
    // public List<String> listOfAddableUsers(ListOfUsernameRequest listOfUsernameRequest){
    //     String owner = jwtService.extractUsername(listOfUsernameRequest.getToken());
    //     Optional<User> user = userRepository.findUserByUsername(owner);
    //     List<MemberGroup>members = user.get().getMemberGroups();
    //     List<String>listOfAddableUsers = new ArrayList<>();
    //     for(String username : listOfAllUsernames()){
    //         for(MemberGroup member : members){
    //             if(member.().equals(listOfUsernameRequest.getShareID())){
    //                 if(username.equalsIgnoreCase(member.getUsername())){
    //                     continue;
    //                 }
    //             }
    //             listOfAddableUsers.add(username);
    //         }
    //     }
    //     log.info("Fetching ListOfAddableUsers from Database: ",listOfAddableUsers);
    //     return listOfAddableUsers;
    // }

    public boolean checkIfUserExists(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isPresent())return true;
        else return false;
    }
}
