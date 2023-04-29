package com.swa.filter.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swa.filter.Interface.MyGroupServiceInterface;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.UserGroupInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MyGroupService implements MyGroupServiceInterface{
    private final MyGroupRepository myGroupRepository;
    @Override
    public MyGroups createGroup(MyGroups mygroups) {
        log.info("Creating Group:{}",mygroups.getGroupName());
        return myGroupRepository.save(mygroups);
    }
    @Override
    public MyGroups deleteMemberFromGroup(UserGroupInfo userGroupInfo) {
        log.info("Deleting user:{} from Group{}",userGroupInfo.getUserName(),myGroupRepository.findByGroupName(userGroupInfo.getGroupName()));
        MyGroups mygroup=myGroupRepository.findByGroupName(userGroupInfo.getGroupName());
        return myGroupRepository.save(mygroup);
    }
    @Override
    public List<MyGroups> getAllGroups() {
        log.info("Get all groups");
        return myGroupRepository.findAll();
    }
    @Override
    public MyGroups getGroup(String groupName) {
        log.info("Get Group:{} ",groupName);
        List<MyGroups> groups = getAllGroups();
        for (MyGroups group : groups) {
            if(group.getGroupName().equalsIgnoreCase(groupName)){
                return myGroupRepository.findByGroupName(groupName);
            }
        }
        return null;
    }
    @Override
    public MyGroups addUserToGroup(UserGroupInfo userGroupInfo) {
        MyGroups mygroup = getGroup(userGroupInfo.getGroupName());
        mygroup.getInfo().add(userGroupInfo);
        return myGroupRepository.save(mygroup);
    }
}
