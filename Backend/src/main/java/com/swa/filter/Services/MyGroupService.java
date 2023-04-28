package com.swa.filter.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swa.filter.Interface.MyGroupServiceInterface;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MyGroupService implements MyGroupServiceInterface{
    private final MyGroupRepository myGroupRepository;
    @Override
    public void addUserToGroup(String groupName,User user) {
        log.info("Adding user:{} to the Group{}",user,groupName);
        MyGroups mygroup = myGroupRepository.findByGroupName(groupName);
        mygroup.getUsers().add(user);
    }
    @Override
    public MyGroups createGroup(MyGroups mygroups) {
        log.info("Creating Group:{}",mygroups.getGroupName());
        return myGroupRepository.save(mygroups);
    }
    @Override
    public void deleteMemberFromGroup(User user,String groupName) {
        log.info("Deleting user:{} from Group{}",user,myGroupRepository.findByGroupName(groupName));
        MyGroups mygroup=myGroupRepository.findByGroupName(groupName);
        mygroup.getUsers().remove(user);
    }
    @Override
    public List<MyGroups> getAllGroups() {
        log.info("Get all groups");
        return myGroupRepository.findAll();
    }
    @Override
    public MyGroups getGroup(String groupName) {
        log.info("Get Group:{} ",groupName);
        return myGroupRepository.findByGroupName(groupName);
    }
}
