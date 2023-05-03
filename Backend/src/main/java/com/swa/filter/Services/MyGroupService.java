package com.swa.filter.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.MyGroupDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MyGroupService{
    private final MyGroupRepository myGroupRepository;
    private final UserService userService;
 
    public String createGroup(String groupname) {
        if(!checkIfGroupExists(groupname)){
            MyGroups my = new MyGroups();
            my.setGroupname(groupname);
            myGroupRepository.save(my);
            return "Group succsessful created";
        }else{
            return "Group with name "+groupname+" exists aleady!";
        }
    }
  
    public String deleteMemberFromGroup(MyGroupDetails userGroupInfo) {
        if(checkIfGroupExists(userGroupInfo.getGroupname())){
            if(checkIfUserExistsInGroup(userGroupInfo)){
                MyGroups mygroup=myGroupRepository.findByGroupname(userGroupInfo.getGroupname());
                List<MyGroupDetails>getinfo = mygroup.getInfo();
                for (MyGroupDetails item : getinfo) {
                    if(item.getUsername().equalsIgnoreCase(userGroupInfo.getUsername())){
                        mygroup.getInfo().remove(getinfo.indexOf(item));
                        myGroupRepository.save(mygroup);
                        return userGroupInfo.getUsername()+" succsessful deleted form "+userGroupInfo.getGroupname();
                    };
                }
            }
        }
        return userGroupInfo.getUsername()+" or "+userGroupInfo.getGroupname()+" doesn't exists!";
    }
  
    public List<MyGroups> getAllGroups() {
        return myGroupRepository.findAll();
    }
  
    public MyGroups getGroup(String groupname) {
        List<MyGroups> groups = getAllGroups();
        System.out.println(groups);
        for (MyGroups group : groups) {
            if(group.getGroupname().equalsIgnoreCase(groupname)){
                return myGroupRepository.findByGroupname(groupname);
            }
        }
        return null;
    }
    public boolean checkIfGroupExists(String groupname){
        List<MyGroups> groups = getAllGroups();
        for (MyGroups group : groups) {
            if(group.getGroupname().equalsIgnoreCase(groupname)){
                return true;
            }
        }
        return false;
    }
   
    public String addUserToGroup(MyGroupDetails userGroupInfo) {
        if(!checkIfGroupExists(userGroupInfo.getGroupname()))return "Group with name "+userGroupInfo.getGroupname()+" doesn't exists!";
        if(!userService.checkIfUserExists(userGroupInfo.getUsername()))return userGroupInfo.getUsername()+" doesn't exists!";
        if(checkIfUserExistsInGroup(userGroupInfo))return userGroupInfo.getUsername()+" exists already in "+userGroupInfo.getGroupname();
        MyGroups mygroup = getGroup(userGroupInfo.getGroupname());
        mygroup.getInfo().add(userGroupInfo);
        myGroupRepository.save(mygroup);
        return userGroupInfo.getUsername()+" succsesfull added to "+userGroupInfo.getGroupname();
    
    
    }
  
    public boolean checkIfUserExistsInGroup(MyGroupDetails userGroupInfo) {
        MyGroups mygroup = getGroup(userGroupInfo.getGroupname());
        List<MyGroupDetails>getinfo = mygroup.getInfo();
        for (MyGroupDetails item : getinfo) {
            if(item.getUsername().equalsIgnoreCase(userGroupInfo.getUsername())){
                return true;
            };
        }
        return false;
    }
}
