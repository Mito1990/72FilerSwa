package com.swa.filter.Interface;

import java.util.List;

import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.UserGroupInfo;

public interface MyGroupServiceInterface {
    List<MyGroups>getAllGroups();
    MyGroups getGroup(String groupName);
    MyGroups deleteMemberFromGroup(UserGroupInfo userGroupInfo);
    MyGroups createGroup(MyGroups mygroup);
    MyGroups addUserToGroup(UserGroupInfo userGroupInfo);
}
