package com.swa.filter.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swa.filter.ObjectModel.MemberGroupRequest;
import com.swa.filter.mySQLTables.MyGroupMembers;
import com.swa.filter.ObjectModel.Role;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;
import com.swa.filter.Repository.MyGroupMembersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MyGroupService{
    private final MyGroupRepository myGroupRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MyGroupMembersRepository myGroupMembersRepository;
 
    public String createGroup(GroupRequest createGroupRequest) {
        String username = jwtService.extractUsername(createGroupRequest.getToken());
        if(!checkIfGroupExists(createGroupRequest.getGroupname(),username)){
            MyGroups myGroup = new MyGroups();
            myGroup.setMembers(null);
            myGroup.setGroupname(createGroupRequest.getGroupname());
            myGroup.setAdmin(username);
            myGroup.setRole(Role.ADMIN);
            myGroup.setFolderID(createGroupRequest.getFolderID());
            myGroupRepository.save(myGroup);
            Optional<User> user = userRepository.findUserByUsername(username);
            user.get().getMygroups().add(myGroup);
            userRepository.save(user.get());
            return "Group succsessful created";
        }else{
            return "Group with name "+createGroupRequest.getGroupname()+" exists aleady!";
        }
    }
  
    public String deleteMemberFromGroup(MemberGroupRequest memberGroupRequest) {
        String owner = jwtService.extractUsername(memberGroupRequest.getGroupRequest().getToken());
        if(checkIfGroupExists(memberGroupRequest.getGroupRequest().getGroupname(),owner)){
            if(checkIfUserExistsInGroup(memberGroupRequest.getGroupRequest().getGroupname(),owner,memberGroupRequest.getMemberRequest().getUsername())){
                MyGroups mygroup=myGroupRepository.findByGroupnameAndAdmin(memberGroupRequest.getGroupRequest().getGroupname(),owner);
                List<MyGroupMembers>members = mygroup.getMembers();
                for (MyGroupMembers member : members) {
                    if(member.getUsername().equalsIgnoreCase(memberGroupRequest.getMemberRequest().getUsername())){
                        mygroup.getMembers().remove(members.indexOf(member));
                        myGroupRepository.save(mygroup);
                        return memberGroupRequest.getMemberRequest().getUsername()+" succsessful deleted form "+memberGroupRequest.getGroupRequest().getGroupname();
                    };
                }
            }
        }
        return memberGroupRequest.getMemberRequest().getUsername()+" or "+memberGroupRequest.getGroupRequest().getGroupname()+" doesn't exists!";
    }
  
    public List<MyGroups> getAllGroups() {
        // List
        return myGroupRepository.findAll();
    }
  
    public MyGroups getGroup(GroupRequest groupRequest) {
        String username = jwtService.extractUsername(groupRequest.getToken());
        if(checkIfGroupExists(groupRequest.getGroupname(),username)){
            List<MyGroups> groups = getAllGroups();
            for (MyGroups group : groups) {
                if(group.getGroupname().equalsIgnoreCase(groupRequest.getGroupname())){
                    return myGroupRepository.findByGroupnameAndAdmin(groupRequest.getGroupname(),username);
                }
            }
        };
        return null;
    }
    public boolean checkIfGroupExists(String groupname,String username){
        List<MyGroups> groups = getAllGroups();
        for (MyGroups group : groups) {
            if(group.getGroupname().equalsIgnoreCase(groupname) && group.getAdmin().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }
   
    public String addUserToGroup(MemberGroupRequest memberGroupRequest) {
        String owner = jwtService.extractUsername(memberGroupRequest.getGroupRequest().getToken());
        System.out.println("Test from Service owner: "+owner);
        if(!checkIfGroupExists(memberGroupRequest.getGroupRequest().getGroupname(),owner)){
            System.out.println("Test from Service CheckIfGroupExists: ");
            return "Group with name "+memberGroupRequest.getGroupRequest().getGroupname()+" doesn't exists!";
        }
        if(!userService.checkIfUserExists(memberGroupRequest.getMemberRequest().getUsername())){
            System.out.println("Test from Service checkIfUserExists: ");
            return memberGroupRequest.getMemberRequest().getUsername()+" doesn't exists!";
        }
        if(checkIfUserExistsInGroup(memberGroupRequest.getGroupRequest().getGroupname(),owner,memberGroupRequest.getMemberRequest().getUsername())){
            System.out.println("Test from Service checkifUserExistsIngroup: ");
            return memberGroupRequest.getMemberRequest().getUsername()+" exists already in "+memberGroupRequest.getGroupRequest().getGroupname();
        }
        MyGroups myGroupOwner = myGroupRepository.findByGroupnameAndAdmin(memberGroupRequest.getGroupRequest().getGroupname(), owner);
        var newGroupMembers = MyGroupMembers.builder().username(memberGroupRequest.getMemberRequest().getUsername()).role(Role.USER).build();
        myGroupMembersRepository.save(newGroupMembers);

        myGroupOwner.getMembers().add(newGroupMembers);
        myGroupOwner.setAdmin(owner);
        myGroupOwner.setPath(memberGroupRequest.getPath());
        myGroupOwner.setFolderID(memberGroupRequest.getFolderID());
        myGroupRepository.save(myGroupOwner);

        var newGroupOwner = MyGroupMembers.builder().username(owner).role(Role.ADMIN).build();
        myGroupMembersRepository.save(newGroupOwner);
        ArrayList<MyGroupMembers> member = new ArrayList<>();
        member.add(newGroupOwner);
        MyGroups myGroupMember = new MyGroups();
        myGroupMember.setMembers(member);
        myGroupMember.setGroupname(memberGroupRequest.getGroupRequest().getGroupname());
        myGroupMember.setAdmin(owner);
        myGroupMember.setRole(Role.USER);
        myGroupMember.setFolderID(memberGroupRequest.getGroupRequest().getFolderID());
        myGroupRepository.save(myGroupMember);
        Optional<User> user = userRepository.findUserByUsername(memberGroupRequest.getMemberRequest().getUsername());
        user.get().getMygroups().add(myGroupMember);
        userRepository.save(user.get());
        return memberGroupRequest.getMemberRequest().getUsername()+" succsesfull added to "+memberGroupRequest.getGroupRequest().getGroupname();
    }
  
    public boolean checkIfUserExistsInGroup(String groupname,String owner,String username) {
        MyGroups mygroup = myGroupRepository.findByGroupnameAndAdmin(groupname,owner);
        List<MyGroupMembers>members = mygroup.getMembers();
        for (MyGroupMembers member: members) {
            if(member.getUsername().equalsIgnoreCase(username)){
                return true;
            };
        }
        return false;
    }
    
}
