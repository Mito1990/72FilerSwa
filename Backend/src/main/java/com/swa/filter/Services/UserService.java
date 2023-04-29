package com.swa.filter.Services;

import com.swa.filter.Interface.UserServiceInterface;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.Role;
import com.swa.filter.mySQLTables.User;
import com.swa.filter.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final RoleRepository userRoleRepository;
    @Override
    public User addUser(User user) {
        log.info("Saving user:{} to the Database",user);
        return userRepository.save(user);
    }

    @Override
    public void addRoleToUser(String username, String name) {
        log.info("Add role:{} to user:{}",name,username);
        User user = userRepository.findUserByUserName(username);
        Role userRole = userRoleRepository.findByName(name);
        user.getRole().add(userRole);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user:{} from Database",username);
        return userRepository.findUserByUserName(username);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users from Database");
        return userRepository.findAll();
    }

    @Override
    public boolean checkIfUserExists(String userName) {
        List<User>list = getAllUsers();
        for (User user : list) {
            if(user.getUserName().equalsIgnoreCase(userName))return true;
        }
        return false;
    }
}
