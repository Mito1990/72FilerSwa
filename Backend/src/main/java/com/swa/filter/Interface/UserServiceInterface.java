package com.swa.filter.Interface;

import java.util.List;
import com.swa.filter.mySQLTables.User;

public interface UserServiceInterface {
    User addUser(User user) throws Exception;
    void addRoleToUser(String username,String role);
    User getUser(String username);
    List<User>getAllUsers();
    boolean checkIfUserExists(String username);
}
