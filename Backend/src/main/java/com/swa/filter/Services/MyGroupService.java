package com.swa.filter.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swa.filter.ObjectModel.MemberGroupRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.mySQLTables.FolderDir;
import com.swa.filter.mySQLTables.MyGroupMembers;
import com.swa.filter.ObjectModel.Role;
import com.swa.filter.ObjectModel.AddFolderResponse;
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
    private final FileService fileService;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MyGroupMembersRepository myGroupMembersRepository;
    private final FolderDirRepository folderDirRepository;
    public MyGroups createGroup(GroupRequest createGroupRequest) {
        System.out.println("creategrouop");
        System.out.println(createGroupRequest);
        String username = jwtService.extractUsername(createGroupRequest.getToken());
        if(!checkIfGroupNameExists(createGroupRequest.getName(),username)){
        List<MyGroupMembers> memberList = new ArrayList<>();
            var myGroup = MyGroups.builder()
                .name(createGroupRequest.getName())
                .admin(username)
                .role(Role.ADMIN)
                .members(memberList)
                .sharedFolders(null)
                .build();
            myGroupRepository.save(myGroup);
            var member = MyGroupMembers.builder().username(username).role(Role.ADMIN).groupID(myGroup.getGroup_id()).build();
            myGroupMembersRepository.save(member);
            myGroup.getMembers().add(member);
            myGroupMembersRepository.save(member);
            Optional<User> user = userRepository.findUserByUsername(username);
            user.get().getMygroups().add(myGroup);
            userRepository.save(user.get());
            // return "Group successful created";
            return myGroup;
        }
            return null;
    }
    public boolean checkIfGroupNameExists(String name,String Username){
        Optional<User> user = userRepository.findUserByUsername(Username);
        List<MyGroups> groups = user.get().getMygroups();
        for(MyGroups group :groups){
            if(group.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
    public String deleteMemberFromGroup(MemberGroupRequest memberGroupRequest) {
        String owner = jwtService.extractUsername(memberGroupRequest.getToken());
        if(checkIfGroupExists(memberGroupRequest.getGroupID())){
            if(checkIfUserExistsInGroup(memberGroupRequest.getName(),owner,memberGroupRequest.getUser())){
                MyGroups mygroup=myGroupRepository.findByNameAndAdmin(memberGroupRequest.getName(),owner);
                List<MyGroupMembers>members = mygroup.getMembers();
                for (MyGroupMembers member : members) {
                    if(member.getUsername().equalsIgnoreCase(memberGroupRequest.getUser())){
                        mygroup.getMembers().remove(members.indexOf(member));
                        myGroupRepository.save(mygroup);
                        return memberGroupRequest.getUser()+" successful deleted form "+memberGroupRequest.getName();
                    };
                }
            }
        }
        return memberGroupRequest.getUser()+" or "+memberGroupRequest.getName()+" doesn't exists!";
    }
    public List<MyGroups> getAllGroups() {
        // List
        return myGroupRepository.findAll();
    }
    public List<MyGroups> getGroup(GroupRequest groupRequest) {
        String username = jwtService.extractUsername(groupRequest.getToken());
        List<MyGroupMembers> allMembers = myGroupMembersRepository.findAll();
        List<MyGroups> groups = new ArrayList<>();
        for(MyGroupMembers member : allMembers){
            if(member.getUsername().equalsIgnoreCase(username)){
                Optional<MyGroups> group = myGroupRepository.findById(member.getGroupID());
                groups.add(group.get());
            }
        }
            return groups;
    }
    public List<FolderDir>getFolderFromGroup(GroupRequest groupRequest){
        System.out.println("\n\n\ngetFolderFromGroup");
        System.out.println(groupRequest);
        Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
        List<FolderDir> folders = new ArrayList<>();
        for(FolderDir folder : group.get().getSharedFolders()){
            if(folder.getParent()==groupRequest.getParent()){
                folders.add(folder);
            }
        }
        System.out.println(folders);
        return folders;
    }

    public List<FolderDir>addFolderToSharedFolder(GroupRequest groupRequest){
        Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
        NewFolderRequest newFolderRequest = new NewFolderRequest(groupRequest.getName(), groupRequest.getParent(), groupRequest.getPath(), groupRequest.getToken(), groupRequest.isShared());
        fileService.newFolder(newFolderRequest);
        var newFolder = FolderDir.builder().name(groupRequest.getName()).parent(groupRequest.getParent()).path(groupRequest.getPath()).shared(groupRequest.isShared()).build();
        folderDirRepository.save(newFolder);
        group.get().getSharedFolders().add(newFolder);
        myGroupRepository.save(group.get());
        System.out.println("addFolderToSharedFolder");
        System.out.println(getFolderFromGroup(groupRequest));
        System.out.println("---------------------------");
        return getFolderFromGroup(groupRequest);
    }
    public boolean checkIfGroupExists(int groupID){
        System.out.println("GroupID: "+groupID);
        Optional<MyGroups> group = myGroupRepository.findById(groupID);
        System.out.println(group);
        if(group.isPresent())return true;
        else return false;
    }
    public String addUserToGroup(MemberGroupRequest memberGroupRequest) {
        System.out.println("memberGroupRequest");
        System.out.println(memberGroupRequest);
        String owner = jwtService.extractUsername(memberGroupRequest.getToken());
        System.out.println("Test from Service owner: "+owner);
        if(!checkIfGroupExists(memberGroupRequest.getGroupID())){
            System.out.println("Test from Service CheckIfGroupExists: ");
            return "Group with name "+memberGroupRequest.getName()+" doesn't exists!";
        }
        if(!userService.checkIfUserExists(memberGroupRequest.getUser())){
            System.out.println("Test from Service checkIfUserExists: ");
            return memberGroupRequest.getUser()+" doesn't exists!";
        }
        if(checkIfUserExistsInGroup(memberGroupRequest.getName(),owner,memberGroupRequest.getUser())){
            System.out.println("Test from Service checkifUserExistsIngroup: ");
            return memberGroupRequest.getUser()+" exists already in "+memberGroupRequest.getName();
        }
        Optional<User> owne = userRepository.findUserByUsername(memberGroupRequest.getUser());
        var groupOfOwner = myGroupRepository.findById(memberGroupRequest.getGroupID());
        // var groupMemberOwner = MyGroupMembers.builder().username(owner).role(Role.ADMIN).build();
        var groupMemberMember = MyGroupMembers.builder().username(memberGroupRequest.getUser()).role(Role.USER).groupID(memberGroupRequest.getGroupID()).build();
        // var groupOfMember = MyGroups.builder().admin(owner).group_id(memberGroupRequest.getGroupID()).build();
        myGroupMembersRepository.save(groupMemberMember);
        groupOfOwner.get().getMembers().add(groupMemberMember);
        userRepository.save(owne.get());
        return memberGroupRequest.getUser()+" successful added to "+memberGroupRequest.getName();
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
