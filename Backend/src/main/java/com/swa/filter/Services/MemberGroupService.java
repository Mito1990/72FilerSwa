package com.swa.filter.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swa.filter.ObjectModel.CreateMemberGroupRequest;
import com.swa.filter.ObjectModel.DeleteMemberFromGroupRequest;
import com.swa.filter.ObjectModel.DeleteMemberGroupRequest;
import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.MemberGroup;
import com.swa.filter.ObjectModel.AddUserToMemberGroupRequest;
import com.swa.filter.ObjectModel.GetListOfMemberGroupsRequest;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.User;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.MemberGroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberGroupService{
    private final FileElementService fileElementService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final FolderRepository folderRepository;
    //Returns the folder of the MemberGroup
    public String createMemberGroup(CreateMemberGroupRequest createMemberGroupRequest) {
        System.out.println("\n\n\ncreateMemberGroup");
        System.out.println("-----------------------------------------------------");
        String owner = jwtService.extractUsername(createMemberGroupRequest.getToken());
        Optional<User> user = userRepository.findUserByUsername(owner);
        Folder newShareFolder = new Folder(createMemberGroupRequest.getGroupName(), null,false, true);
        List<String> list = new ArrayList<>();
        MemberGroup memberGroup = MemberGroup.builder().admin(owner).groupName(createMemberGroupRequest.getGroupName()).shareFolder(newShareFolder).usernames(list).build();
        user.get().getMemberGroups().add(memberGroup);
        folderRepository.save(newShareFolder);
        memberGroupRepository.save(memberGroup);
        userRepository.save(user.get());
        memberGroup.setPath(fileElementService.createMemberGroupFolder(createMemberGroupRequest.getGroupName(),memberGroup.getMemberGroupID()));
        memberGroupRepository.save(memberGroup);
        System.out.println("Member Group is created!");
        System.out.println(memberGroup);
        System.out.println("-----------------------------------------------------\n\n\n");
        return "MemberGroup: {"+memberGroup+"} successful created!";
    }
    public List<MemberGroup>getListOfGroupsIncludeUser(GetListOfMemberGroupsRequest getListOfMemberGroupsRequest){
        String owner = jwtService.extractUsername(getListOfMemberGroupsRequest.getToken());
        List<MemberGroup>getListOfGroupsIncludeUser = new ArrayList<>();
        List<MemberGroup> allMemberGroups = memberGroupRepository.findAll();
        for(MemberGroup memberGroup : allMemberGroups){
            if(memberGroup.getUsernames().contains(owner) || memberGroup.getAdmin().equalsIgnoreCase(owner)){
                getListOfGroupsIncludeUser.add(memberGroup);
            };
        }
        System.out.println(getListOfGroupsIncludeUser);
        return getListOfGroupsIncludeUser;
    }
    public List<String> addUserToMemberGroup(AddUserToMemberGroupRequest addUserToMemberGroupRequest){
        Optional<MemberGroup> memberGroup = memberGroupRepository.findById(addUserToMemberGroupRequest.getMemberGroupID());
        memberGroup.get().getUsernames().add(addUserToMemberGroupRequest.getUser());
        memberGroupRepository.save(memberGroup.get());
        return memberGroup.get().getUsernames();
    }
    public List<String> deleteMemberFromGroup(DeleteMemberFromGroupRequest deleteMemberFromGroupRequest){
        Optional<MemberGroup> memberGroup = memberGroupRepository.findById(deleteMemberFromGroupRequest.getMemberGroupID());
        if(memberGroup.get().getUsernames().remove(deleteMemberFromGroupRequest.getUser())){
            memberGroupRepository.save(memberGroup.get());
            return memberGroup.get().getUsernames();
        }
        return null;
    }
    public String deleteMemberGroup(DeleteMemberGroupRequest deleteMemberGroupRequest){
        System.out.println("\n\n\n\nHello From DeleteMemberGroup");
        System.out.println(deleteMemberGroupRequest);
        String owner = jwtService.extractUsername(deleteMemberGroupRequest.getToken());
        Optional<User> user = userRepository.findUserByUsername(owner);
        Optional<MemberGroup> deleteMemberGroup = memberGroupRepository.findById(deleteMemberGroupRequest.getMemberGroupID());
        if(deleteMemberGroup.get().getAdmin().equalsIgnoreCase(owner)){
            if(user.get().getMemberGroups().remove(deleteMemberGroup.get())){
                memberGroupRepository.deleteById(deleteMemberGroupRequest.getMemberGroupID());
                fileElementService.deleteGroupRepository(deleteMemberGroup.get());
                return "Group successful deleted!";
            }
        }
        return "Only the Admin:{"+deleteMemberGroup.get().getAdmin()+"} has the right to delete the group!";
    }
}
