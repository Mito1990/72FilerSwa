package com.swa.filter.Testing;

import com.swa.filter.ObjectModel.*;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.Repository.MemberGroupRepository;
import com.swa.filter.Services.MemberGroupService;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.mySQLTables.*;
import com.swa.filter.Services.FileElementService;
import com.swa.filter.Services.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberGroupServiceTests {
    @Mock
    private FileElementService fileElementService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberGroupRepository memberGroupRepository;

    @Mock
    private FolderRepository folderRepository;

    @InjectMocks
    private MemberGroupService memberGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMemberGroup_ShouldCreateMemberGroup() {
        // Arrange
        CreateMemberGroupRequest createMemberGroupRequest = new CreateMemberGroupRequest();
        createMemberGroupRequest.setToken("dummyToken");
        createMemberGroupRequest.setGroupName("TestGroup");

        User user = new User();
        user.setUsername("admin");
        user.setMemberGroups(new ArrayList<>());

        Folder newShareFolder = new Folder("TestGroup", null, false, true);

        MemberGroup memberGroup = MemberGroup.builder()
                .admin("admin")
                .groupName("TestGroup")
                .shareFolder(newShareFolder)
                .usernames(new ArrayList<>())
                .build();

        when(jwtService.extractUsername(createMemberGroupRequest.getToken())).thenReturn("admin");
        when(userRepository.findUserByUsername("admin")).thenReturn(Optional.of(user));
        when(memberGroupRepository.save(any(MemberGroup.class))).thenReturn(memberGroup);
        when(folderRepository.save(any(Folder.class))).thenReturn(newShareFolder);
        when(fileElementService.createMemberGroupFolder(eq("TestGroup"),anyInt())).thenReturn("folderPath");

        MemberGroupService newMemberGroupService = new MemberGroupService(
            fileElementService,
            jwtService,
            userRepository,
            memberGroupRepository,
            folderRepository);
        // Act
        String result = newMemberGroupService.createMemberGroup(createMemberGroupRequest);

        // Assert
        assertEquals("MemberGroup: {" + memberGroup + "} successful created!", result);
        assertEquals(1, user.getMemberGroups().size());
        assertEquals(memberGroup, user.getMemberGroups().get(0));;
        verify(userRepository).save(user);
        verify(folderRepository).save(newShareFolder);
    }

    @Test
    void addUserToMemberGroup_ShouldAddUserToMemberGroup() {
        // Arrange
        AddUserToMemberGroupRequest addUserToMemberGroupRequest = new AddUserToMemberGroupRequest();
        addUserToMemberGroupRequest.setMemberGroupID(1);
        addUserToMemberGroupRequest.setUser("newUser");

        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setUsernames(new ArrayList<>());

        when(memberGroupRepository.findById(addUserToMemberGroupRequest.getMemberGroupID()))
                .thenReturn(Optional.of(memberGroup));
        when(memberGroupRepository.save(any(MemberGroup.class))).thenReturn(memberGroup);

        // Act
        List<String> result = memberGroupService.addUserToMemberGroup(addUserToMemberGroupRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("newUser", result.get(0));
        verify(memberGroupRepository).save(memberGroup);
    }

    @Test
    void deleteMemberFromGroup_ShouldDeleteMemberFromGroup() {
        // Arrange
        DeleteMemberFromGroupRequest deleteMemberFromGroupRequest = new DeleteMemberFromGroupRequest();
        deleteMemberFromGroupRequest.setMemberGroupID(1);
        deleteMemberFromGroupRequest.setUser("user1");

        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setUsernames(new ArrayList<>());
        memberGroup.getUsernames().add("user1");
        memberGroup.getUsernames().add("user2");

        when(memberGroupRepository.findById(deleteMemberFromGroupRequest.getMemberGroupID()))
                .thenReturn(Optional.of(memberGroup));
        when(memberGroupRepository.save(any(MemberGroup.class))).thenReturn(memberGroup);

        MemberGroupService newMemberGroupService = new MemberGroupService(
            fileElementService,
            jwtService,
            userRepository,
            memberGroupRepository,
            folderRepository);

        // Act
        List<String> result = newMemberGroupService.deleteMemberFromGroup(deleteMemberFromGroupRequest);

        // Assert
        assertEquals(1, result.size());
        assertEquals("user2", result.get(0));
        verify(memberGroupRepository).save(memberGroup);
    }

    @Test
    void deleteMemberGroup_ShouldDeleteMemberGroup() {
        // Arrange
        DeleteMemberGroupRequest deleteMemberGroupRequest = new DeleteMemberGroupRequest();
        deleteMemberGroupRequest.setToken("dummyToken");
        deleteMemberGroupRequest.setMemberGroupID(1);

        User user = new User();
        user.setUsername("admin");
        user.setMemberGroups(new ArrayList<>());

        MemberGroup deleteMemberGroup = new MemberGroup();
        deleteMemberGroup.setAdmin("admin");
        user.getMemberGroups().add(0, deleteMemberGroup);

        when(jwtService.extractUsername(deleteMemberGroupRequest.getToken())).thenReturn("admin");
        when(userRepository.findUserByUsername("admin")).thenReturn(Optional.of(user));
        when(memberGroupRepository.findById(deleteMemberGroupRequest.getMemberGroupID()))
                .thenReturn(Optional.of(deleteMemberGroup));
        when(fileElementService.deleteGroupRepository(deleteMemberGroup)).thenReturn(true);

        MemberGroupService newMemberGroupService = new MemberGroupService(
            fileElementService,
            jwtService,
            userRepository,
            memberGroupRepository,
            folderRepository);

        // Act
        String result = newMemberGroupService.deleteMemberGroup(deleteMemberGroupRequest);

        // Assert
        assertEquals("Group successful deleted!", result);
        assertEquals(0, user.getMemberGroups().size());
        verify(memberGroupRepository).deleteById(deleteMemberGroupRequest.getMemberGroupID());
        verify(fileElementService).deleteGroupRepository(deleteMemberGroup);
    }
}
