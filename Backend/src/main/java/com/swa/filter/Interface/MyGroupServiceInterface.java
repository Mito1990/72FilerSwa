package com.swa.filter.Interface;

import java.util.List;

import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;

public interface MyGroupServiceInterface {
    List<MyGroups>getAllGroups();
    MyGroups addUserToGroup(User user);
    MyGroups getGroup(String groupName);
    void deleteGroup(String groupName);
    MyGroups createGroup(String groupName);
}
