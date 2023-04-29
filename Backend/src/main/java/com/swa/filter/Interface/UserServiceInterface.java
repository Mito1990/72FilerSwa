package com.swa.filter.Interface;

import java.util.List;
import com.swa.filter.mySQLTables.User;

public interface UserServiceInterface {
    User addUser(User user) throws Exception;
    void addRoleToUser(String userName,String role);
    User getUser(String userName);
    List<User>getAllUsers();
    boolean checkIfUserExists(String userName);
}
