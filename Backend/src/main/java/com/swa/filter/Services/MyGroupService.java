package com.swa.filter.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.MyGroupDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MyGroupService{
    private final MyGroupRepository myGroupRepository;
 
    public MyGroups createGroup(MyGroups mygroups) {
        log.info("Creating Group:{}",mygroups.getGroupName());
        return myGroupRepository.save(mygroups);
    }
  
    public MyGroups deleteMemberFromGroup(MyGroupDetails userGroupInfo) {
        log.info("Deleting user:{} from Group{}",userGroupInfo.getUserName(),myGroupRepository.findByGroupName(userGroupInfo.getGroupName()));
        MyGroups mygroup=myGroupRepository.findByGroupName(userGroupInfo.getGroupName());
        List<MyGroupDetails>getinfo = mygroup.getInfo();
        for (MyGroupDetails item : getinfo) {
            if(item.getUserName().equalsIgnoreCase(userGroupInfo.getUserName())){
                mygroup.getInfo().remove(getinfo.indexOf(item));
                return myGroupRepository.save(mygroup);
            };
        }
        return null;
    }
  
    public List<MyGroups> getAllGroups() {
        log.info("Get all groups");
        return myGroupRepository.findAll();
    }
  
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
    public boolean checkIfGroupExists(String groupName){
        List<MyGroups> groups = getAllGroups();
        for (MyGroups group : groups) {
            if(group.getGroupName().equalsIgnoreCase(groupName)){
                return true;
            }
        }
        return false;
    }
   
    public MyGroups addUserToGroup(MyGroupDetails userGroupInfo) {
        MyGroups mygroup = getGroup(userGroupInfo.getGroupName());
        mygroup.getInfo().add(userGroupInfo);
        return myGroupRepository.save(mygroup);
    }
  
    public boolean checkIfUserExistsInGroup(MyGroupDetails userGroupInfo) {
        MyGroups mygroup = getGroup(userGroupInfo.getGroupName());
        List<MyGroupDetails>getinfo = mygroup.getInfo();
        for (MyGroupDetails item : getinfo) {
            if(item.getUserName().equalsIgnoreCase(userGroupInfo.getUserName())){
                return true;
            };
        }
        return false;
    }
}
