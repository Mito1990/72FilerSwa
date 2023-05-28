package com.swa.filter.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swa.filter.ObjectModel.MemberGroupRequest;
import com.swa.filter.mySQLTables.FolderDir;
import com.swa.filter.mySQLTables.MyGroupMembers;
import com.swa.filter.ObjectModel.Role;
import com.swa.filter.ObjectModel.AddFolderToGroupRequest;
import com.swa.filter.ObjectModel.GroupFolderRequest;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.Repository.MyGroupRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.MyGroups;
import com.swa.filter.mySQLTables.User;
import com.swa.filter.Repository.FolderDirRepository;
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
    private final FolderDirRepository folderDirRepository;
    public MyGroups createGroup(GroupRequest createGroupRequest) {
        String username = jwtService.extractUsername(createGroupRequest.getToken());
        if(!checkIfGroupExists(createGroupRequest.getName(),username)){
            var folder = FolderDir.builder()
                .parent(createGroupRequest.getParent())
                .path(createGroupRequest.getPath())
                .shared(createGroupRequest.isShared())
                .name(createGroupRequest.getName())
                .build();
            folderDirRepository.save(folder);
            List<FolderDir>list = new ArrayList<>();
            list.add(folder);
            var myGroup = MyGroups.builder()
                .name(createGroupRequest.getName())
                .admin(username)
                .role(Role.ADMIN)
                .sharedFolders(list)
                .build();
            myGroupRepository.save(myGroup);
            Optional<User> user = userRepository.findUserByUsername(username);
            user.get().getMygroups().add(myGroup);
            userRepository.save(user.get());
            // return "Group successful created";
            return myGroup;
        }
            return null;
        // }else{
        //     // return "Group with name "+createGroupRequest.getGroupname()+" exists already!";
        // }
    }
    public String deleteMemberFromGroup(MemberGroupRequest memberGroupRequest) {
        String owner = jwtService.extractUsername(memberGroupRequest.getToken());
        if(checkIfGroupExists(memberGroupRequest.getGroupname(),owner)){
            if(checkIfUserExistsInGroup(memberGroupRequest.getGroupname(),owner,memberGroupRequest.getUser())){
                MyGroups mygroup=myGroupRepository.findByNameAndAdmin(memberGroupRequest.getGroupname(),owner);
                List<MyGroupMembers>members = mygroup.getMembers();
                for (MyGroupMembers member : members) {
                    if(member.getUsername().equalsIgnoreCase(memberGroupRequest.getUser())){
                        mygroup.getMembers().remove(members.indexOf(member));
                        myGroupRepository.save(mygroup);
                        return memberGroupRequest.getUser()+" successful deleted form "+memberGroupRequest.getGroupname();
                    };
                }
            }
        }
        return memberGroupRequest.getUser()+" or "+memberGroupRequest.getGroupname()+" doesn't exists!";
    }
    public List<MyGroups> getAllGroups() {
        // List
        return myGroupRepository.findAll();
    }
    public List<MyGroups> getGroup(GroupRequest groupRequest) {
        System.out.println("test\n\n\n");
        String username = jwtService.extractUsername(groupRequest.getToken());
        // if(checkIfGroupExists(groupRequest.getGroupname(),username)){
            Optional<User> user = userRepository.findUserByUsername(username);
            List<MyGroups> groups = user.get().getMygroups();
            return groups;
    }
    public List<FolderDir>getFolderFromGroup(GroupFolderRequest groupFolderRequest){
        String username = jwtService.extractUsername(groupFolderRequest.getToken());
        Optional<User>user = userRepository.findUserByUsername(username);
        List<MyGroups> groups = user.get().getMygroups();
        List<FolderDir>folders = new ArrayList<>();
        for(MyGroups group : groups){
            if(group.getGroup_id()==groupFolderRequest.getGroupID()){
                folders=group.getSharedFolders();
            }
        }
        return folders;
    }
    public boolean checkIfGroupExists(String groupname,String username){
        List<MyGroups> groups = getAllGroups();
        for (MyGroups group : groups) {
            if(group.getName().equalsIgnoreCase(groupname) && group.getAdmin().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }
    public String addUserToGroup(MemberGroupRequest memberGroupRequest) {
        String owner = jwtService.extractUsername(memberGroupRequest.getToken());
        System.out.println("Test from Service owner: "+owner);
        if(!checkIfGroupExists(memberGroupRequest.getGroupname(),owner)){
            System.out.println("Test from Service CheckIfGroupExists: ");
            return "Group with name "+memberGroupRequest.getGroupname()+" doesn't exists!";
        }
        if(!userService.checkIfUserExists(memberGroupRequest.getUser())){
            System.out.println("Test from Service checkIfUserExists: ");
            return memberGroupRequest.getUser()+" doesn't exists!";
        }
        if(checkIfUserExistsInGroup(memberGroupRequest.getGroupname(),owner,memberGroupRequest.getUser())){
            System.out.println("Test from Service checkifUserExistsIngroup: ");
            return memberGroupRequest.getUser()+" exists already in "+memberGroupRequest.getGroupname();
        }
        MyGroups myGroupOwner = myGroupRepository.findByNameAndAdmin(memberGroupRequest.getGroupname(), owner);
        var newGroupMembers = MyGroupMembers.builder().username(memberGroupRequest.getUser()).role(Role.USER).build();
        myGroupMembersRepository.save(newGroupMembers);

        myGroupOwner.getMembers().add(newGroupMembers);
        myGroupOwner.setAdmin(owner);
        myGroupRepository.save(myGroupOwner);

        var newGroupOwner = MyGroupMembers.builder().username(owner).role(Role.ADMIN).build();
        myGroupMembersRepository.save(newGroupOwner);
        ArrayList<MyGroupMembers> member = new ArrayList<>();
        member.add(newGroupOwner);
        MyGroups myGroupMember = new MyGroups();
        myGroupMember.setMembers(member);
        myGroupMember.setName(memberGroupRequest.getGroupname());
        myGroupMember.setAdmin(owner);
        myGroupMember.setRole(Role.USER);
        myGroupRepository.save(myGroupMember);
        Optional<User> user = userRepository.findUserByUsername(memberGroupRequest.getUser());
        user.get().getMygroups().add(myGroupMember);
        userRepository.save(user.get());
        return memberGroupRequest.getUser()+" successful added to "+memberGroupRequest.getGroupname();
    }

    public List<FolderDir> addFolderToGroup(AddFolderToGroupRequest addFolderToGroupRequest){
        String username = jwtService.extractUsername(addFolderToGroupRequest.getToken());
        Optional<User>user = userRepository.findUserByUsername(username);
        List<MyGroups> groups =user.get().getMygroups();
        List<FolderDir> folders = new ArrayList<>();
        var folder = FolderDir.builder().name(addFolderToGroupRequest.getName()).parent(addFolderToGroupRequest.getParentID()).path(addFolderToGroupRequest.getPath()).shared(addFolderToGroupRequest.isShared()).build();
        folderDirRepository.save(folder);
        for(MyGroups group : groups){
            if(group.getGroup_id() == addFolderToGroupRequest.getGroupID()){
                group.getSharedFolders().add(folder);
                folders = group.getSharedFolders();
            }
        }
        return folders;
    }
    public boolean checkIfUserExistsInGroup(String groupname,String owner,String username) {
        MyGroups mygroup = myGroupRepository.findByNameAndAdmin(groupname,owner);
        List<MyGroupMembers>members = mygroup.getMembers();
        for (MyGroupMembers member: members) {
            if(member.getUsername().equalsIgnoreCase(username)){
                return true;
            };
        }
        return false;
    }
}
