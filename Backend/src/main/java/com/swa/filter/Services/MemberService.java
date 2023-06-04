package com.swa.filter.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swa.filter.ObjectModel.MemberGroupRequest;
import com.swa.filter.ObjectModel.NewFolderGroupRequest;
import com.swa.filter.ObjectModel.NewFolderRequest;
import com.swa.filter.ObjectModel.NewShareFolderRequest;
import com.swa.filter.mySQLTables.FileElement;
import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.Member;
import com.swa.filter.ObjectModel.Role;
import com.swa.filter.ObjectModel.AddFolderResponse;
import com.swa.filter.ObjectModel.AddFolderToGroupRequest;
import com.swa.filter.ObjectModel.AddMemberRequest;
import com.swa.filter.ObjectModel.AddNewFolderRequest;
import com.swa.filter.ObjectModel.GetFolderRequest;
import com.swa.filter.ObjectModel.GroupFolderRequest;
import com.swa.filter.ObjectModel.GroupRequest;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.User;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService{
    private final FileService fileService;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final FolderRepository folderRepository;

    public List<Folder> newShareFolder(NewShareFolderRequest newShareFolderRequest) {
        System.out.println("\nnewShareFolder");
        System.out.println("-----------------------------------------------------");
        String owner = jwtService.extractUsername(newShareFolderRequest.getToken());
        Optional<User> user = userRepository.findUserByUsername(owner);
        Folder newShareFolder = new Folder(newShareFolderRequest.getName(), null, true);
        Member member = Member.builder().username(newShareFolderRequest.getName()).shareID(newShareFolder.getId()).role(Role.ADMIN).build();
        user.get().getShareFolders().add(newShareFolder);
        user.get().getMembers().add(member);
        memberRepository.save(member);
        folderRepository.save(newShareFolder);
        userRepository.save(user.get());
        log.info("New share folder successful created!",user.get().getShareFolders());
        System.out.println("-----------------------------------------------------\n");
        return user.get().getShareFolders();
    }
    public List<?>addNewFolder(AddNewFolderRequest addNewFolderRequest){
            System.out.println("\nAddNewFolderRequest");
            System.out.println("-----------------------------------------------------");
            System.out.println("addNewFolderRequest");
            System.out.println(addNewFolderRequest);
            if(addNewFolderRequest.getParent()==null){
                System.out.println("Parent == null");
            }
            String owner = jwtService.extractUsername(addNewFolderRequest.getToken());
            Optional<User> user = userRepository.findUserByUsername(owner);
            user.get().getShareFolders();
            Optional<Folder> parentFolder = folderRepository.findById(addNewFolderRequest.getParent().getId());
            Folder newFolder = new Folder(addNewFolderRequest.getName(),parentFolder.get() ,addNewFolderRequest.isShared());
            parentFolder.get().getChildren().add(newFolder);
            folderRepository.save(newFolder);
            userRepository.save(user.get());
            System.out.println("getFolder(new GetFolderRequest(addNewFolderRequest.getToken(), addNewFolderRequest.getParent().getId()))");
            System.out.println(getFolder(new GetFolderRequest(addNewFolderRequest.getToken(), addNewFolderRequest.getParent().getId())));
            System.out.println("-----------------------------------------------------\n");
            return getFolder(new GetFolderRequest(addNewFolderRequest.getToken(), parentFolder.get().getId()));
    }
    public void addMember(AddMemberRequest addMemberRequest){
        String owner = jwtService.extractUsername(addMemberRequest.getToken());
        Optional<User> user = userRepository.findUserByUsername(owner);
        Member member = Member.builder().username(addMemberRequest.getMember()).shareID(addMemberRequest.getShareID()).role(Role.USER).build();
        log.info("Member: {} successful added!",addMemberRequest.getMember());
        user.get().getMembers().add(member);
        memberRepository.save(member);
        userRepository.save(user.get());
    }
    public List<?> getFolder(GetFolderRequest getFolderRequest){
        System.out.println("getFolder");
        System.out.println("-----------------------------------------------------");
        System.out.println("getFolderRequest");
        System.out.println(getFolderRequest);
        String owner = jwtService.extractUsername(getFolderRequest.getToken());
        Optional<User> user = userRepository.findUserByUsername(owner);
        if(getFolderRequest.getParentID()==null){
            System.out.println("-----------------------------------------------------\n");
            return user.get().getShareFolders();
        }else{
            Optional<Folder> parentFolder = folderRepository.findById(getFolderRequest.getParentID());
            System.out.println("parentFolder");
            System.out.println(parentFolder);
            System.out.println("-----------------------------------------------------\n");
            return parentFolder.get().getChildren();
        }
    }
    // public boolean checkIfGroupNameExists(String name,String Username){
    //     Optional<User> user = userRepository.findUserByUsername(Username);
    //     List<MyGroups> groups = user.get().getMygroups();
    //     for(MyGroups group :groups){
    //         if(group.getName().equalsIgnoreCase(name)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    // public String deleteMemberFromGroup(MemberGroupRequest memberGroupRequest) {
    //     String owner = jwtService.extractUsername(memberGroupRequest.getToken());
    //     if(checkIfGroupExists(memberGroupRequest.getGroupID())){
    //         if(checkIfUserExistsInGroup(memberGroupRequest.getName(),owner,memberGroupRequest.getUser())){
    //             MyGroups mygroup=myGroupRepository.findByNameAndAdmin(memberGroupRequest.getName(),owner);
    //             List<Members>members = mygroup.getMembers();
    //             for (Members member : members) {
    //                 if(member.getUsername().equalsIgnoreCase(memberGroupRequest.getUser())){
    //                     mygroup.getMembers().remove(members.indexOf(member));
    //                     myGroupRepository.save(mygroup);
    //                     return memberGroupRequest.getUser()+" successful deleted form "+memberGroupRequest.getName();
    //                 };
    //             }
    //         }
    //     }
    //     return memberGroupRequest.getUser()+" or "+memberGroupRequest.getName()+" doesn't exists!";
    // }
    // public List<MyGroups> getAllGroups() {
    //     // List
    //     return myGroupRepository.findAll();
    // }
    // public List<MyGroups> getGroup(GroupRequest groupRequest) {
    //     String username = jwtService.extractUsername(groupRequest.getToken());
    //     List<Members> allMembers = myGroupMembersRepository.findAll();
    //     List<MyGroups> groups = new ArrayList<>();
    //     for(Members member : allMembers){
    //         if(member.getUsername().equalsIgnoreCase(username)){
    //             Optional<MyGroups> group = myGroupRepository.findById(member.getGroupID());
    //             groups.add(group.get());
    //         }
    //     }
    //         return groups;
    // }
    // public List<Folder>getFolderFromGroup(GroupRequest groupRequest){
    //     System.out.println("\n\n\ngetFolderFromGroup");
    //     System.out.println(groupRequest);
    //     Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
    //     List<Folder> folders = new ArrayList<>();
    //     for(Folder folder : group.get().getSharedFolders()){
    //         if(folder.getParent()==groupRequest.getParent()){
    //             folders.add(folder);
    //         }
    //     }
    //     System.out.println("test");
    //     System.out.println(folders);
    //     return folders;
    // }
 
    // public List<Folder>addFolderToSharedFolder(GroupRequest groupRequest){
    //     System.out.println("\n\n\naddFolderToSharedFolder");
    //     System.out.println("---------------------------------------------------------------------");
    //     System.out.println("groupRequest");
    //     System.out.println(groupRequest);
    //     Optional<MyGroups> group = myGroupRepository.findById(groupRequest.getGroupID());
    //     NewFolderRequest newFolderRequest = new NewFolderRequest(groupRequest.getName(), groupRequest.getParent(), groupRequest.getPath(), groupRequest.getToken(), groupRequest.isShared());
    //     fileService.newFolder(newFolderRequest);
    //     var newFolder = Folder.builder().name(groupRequest.getName()).parent(groupRequest.getParent()).path(groupRequest.getPath()).shared(groupRequest.isShared()).file(groupRequest.isFile()).build();
    //     System.out.println("newFolder");
    //     System.out.println(newFolder);
    //     folderDirRepository.save(newFolder);
    //     group.get().getSharedFolders().add(newFolder);
    //     myGroupRepository.save(group.get());
    //     System.out.println("---------------------------------------------------------------------");
    //     return getFolderFromGroup(groupRequest);
    // }
    // public boolean checkIfGroupExists(int groupID){
    //     System.out.println("GroupID: "+groupID);
    //     Optional<MyGroups> group = myGroupRepository.findById(groupID);
    //     System.out.println(group);
    //     if(group.isPresent())return true;
    //     else return false;
    // }
    // public String addUserToGroup(MemberGroupRequest memberGroupRequest) {
    //     String owner = jwtService.extractUsername(memberGroupRequest.getToken());
    //     System.out.println("Test from Service owner: "+owner);
    //     if(!checkIfGroupExists(memberGroupRequest.getGroupID())){
    //         System.out.println("Test from Service CheckIfGroupExists: ");
    //         return "Group with name "+memberGroupRequest.getName()+" doesn't exists!";
    //     }
    //     if(!userService.checkIfUserExists(memberGroupRequest.getUser())){
    //         System.out.println("Test from Service checkIfUserExists: ");
    //         return memberGroupRequest.getUser()+" doesn't exists!";
    //     }
    //     if(checkIfUserExistsInGroup(memberGroupRequest.getName(),owner,memberGroupRequest.getUser())){
    //         System.out.println("Test from Service checkifUserExistsIngroup: ");
    //         return memberGroupRequest.getUser()+" exists already in "+memberGroupRequest.getName();
    //     }
    //     Optional<User> owne = userRepository.findUserByUsername(memberGroupRequest.getUser());
    //     var groupOfOwner = myGroupRepository.findById(memberGroupRequest.getGroupID());
    //     // var groupMemberOwner = MyGroupMembers.builder().username(owner).role(Role.ADMIN).build();
    //     var groupMemberMember = Members.builder().username(memberGroupRequest.getUser()).role(Role.USER).groupID(memberGroupRequest.getGroupID()).build();
    //     // var groupOfMember = MyGroups.builder().admin(owner).group_id(memberGroupRequest.getGroupID()).build();
    //     myGroupMembersRepository.save(groupMemberMember);
    //     groupOfOwner.get().getMembers().add(groupMemberMember);
    //     userRepository.save(owne.get());
    //     return memberGroupRequest.getUser()+" successful added to "+memberGroupRequest.getName();
    // }

    // public boolean checkIfUserExistsInGroup(String groupname,String owner,String username) {
    //     MyGroups mygroup = myGroupRepository.findByNameAndAdmin(groupname,owner);
    //     List<Members>members = mygroup.getMembers();
    //     for (Members member: members) {
    //         if(member.getUsername().equalsIgnoreCase(username)){
    //             return true;
    //         };
    //     }
    //     return false;
    // }
}
