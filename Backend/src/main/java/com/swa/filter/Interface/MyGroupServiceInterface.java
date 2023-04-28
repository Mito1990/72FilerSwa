package com.swa.filter.Interface;

import java.util.List;

import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;

public interface MyGroupServiceInterface {
    List<MyGroups>getAllGroups();
    void addUserToGroup(String groupName,User user);
    MyGroups getGroup(String groupName);
    void deleteMemberFromGroup(User user,String groupName);
    MyGroups createGroup(MyGroups mygroup);
}
