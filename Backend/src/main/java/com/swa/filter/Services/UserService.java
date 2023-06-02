package com.swa.filter.Services;

import com.swa.filter.ObjectModel.ListOfUserRequest;
import com.swa.filter.Repository.MyGroupMembersRepository;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.MyGroupMembers;
import com.swa.filter.mySQLTables.MyGroups;
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
    private final MyGroupRepository myGroupRepository;
    private final UserRepository userRepository;
    public Optional<User> getUser(String username) {
        log.info("Fetching user:{} from Database",username);
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users from Database");
        return userRepository.findAll();
    }

    public List<String>listOfUsernames(ListOfUserRequest listOfUserRequest){
        System.out.println("Request");
        System.out.println(listOfUserRequest);
        List<User> listOfAllUsers = getAllUsers();
        List<String> listOfUsernames = new ArrayList<>();
        List<String> listOfUsernamesInMembers = new ArrayList<>();
        Optional<MyGroups> group = myGroupRepository.findById(listOfUserRequest.getGroupID());
        List<MyGroupMembers>members = group.get().getMembers();
        for(MyGroupMembers member : members){
            listOfUsernamesInMembers.add(member.getUsername());
        }
        for(User user : listOfAllUsers){
            if(!listOfUsernamesInMembers.contains(user.getUsername())){
                System.out.println("\n\n\nHello from ListOfAllUsers");
                listOfUsernames.add(user.getUsername());
            }
        }
        return listOfUsernames;
    }
    public boolean checkIfUserExists(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isPresent())return true;
        else return false;
    }
}
