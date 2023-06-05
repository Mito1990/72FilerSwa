package com.swa.filter.Services;

import com.swa.filter.ObjectModel.ListOfUsernameNotAddedToGroupRequest;
import com.swa.filter.ObjectModel.ListOfUsernamesInGroup;
import com.swa.filter.Repository.MemberGroupRepository;
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
    private final MemberGroupRepository memberGroupRepository;
    public Optional<User> getUser(String username) {
        log.info("Fetching user:{} from Database",username);
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users from Database");
        return userRepository.findAll();
    }

    public List<String>getListOfUsernamesIsNotAddedToGroup(ListOfUsernameNotAddedToGroupRequest listOfUsernameNotAddedToGroupRequest){
        System.out.println("listOfUsernameNotAddedToGroup");
        System.out.println(listOfUsernameNotAddedToGroupRequest);
        System.out.println("memberGroup");
        Optional<MemberGroup> memberGroup = memberGroupRepository.findById(listOfUsernameNotAddedToGroupRequest.getMemberGroupID());
        System.out.println(memberGroup);
        List<User>users = getAllUsers();
        List<String>listOfAllUsernames = new ArrayList<>();
        List<String>usernameExistsAlreadyInGroup = memberGroup.get().getUsernames();
        for(User user : users){
            listOfAllUsernames.add(user.getUsername());
        }
        System.out.println("\n\n\n\nlistOfAllUsernames before");
        System.out.println(listOfAllUsernames);
        if(listOfAllUsernames.removeAll(usernameExistsAlreadyInGroup)){
            System.out.println("listOfAllUsernames after\n\n\n\n");
            System.out.println(listOfAllUsernames);
        };
        System.out.println("\n\n\n\nlistOfAllUsernames before admin");
        System.out.println(listOfAllUsernames);
        if(listOfAllUsernames.remove(memberGroup.get().getAdmin())){
            System.out.println("listOfAllUsernames after admin\n\n\n\n");
            System.out.println(listOfAllUsernames);
        };
        log.info("Fetching all listOfAllUsernames from Database: ",listOfAllUsernames);
        return listOfAllUsernames;
    }
    public List<String>getListOfUsernamesInGroup(ListOfUsernamesInGroup listOfUsernamesInGroup){
        Optional<MemberGroup> memberGroup = memberGroupRepository.findById(listOfUsernamesInGroup.getMemberGroupID());
        return memberGroup.get().getUsernames();
    }

    public boolean checkIfUserExists(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isPresent())return true;
        else return false;
    }
}
