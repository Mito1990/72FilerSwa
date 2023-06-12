package com.swa.filter.Testing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.swa.filter.ObjectModel.CreateNewFileRequest;
import com.swa.filter.ObjectModel.CreateNewFolderRequest;
import com.swa.filter.Repository.FileElementRepository;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.MemberGroupRepository;
import com.swa.filter.Repository.MyFileRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.Services.FileElementService;
import com.swa.filter.Services.JwtService;
import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.MyFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileElementServiceTests{
    @Mock
    private FileElementService fileElementService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private MemberGroupRepository memberGroupRepository;
    @Mock
    private MyFileRepository myFileRepository;
    @Mock
    private FileElementRepository fileElementRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileElementService = new FileElementService(
                userRepository,
                jwtService,
                folderRepository,
                memberGroupRepository,
                myFileRepository,
                fileElementRepository
        );
    }


    @Test
    void createUserFolder_shouldCreateDirectory() throws IOException {
        String rootUsers = "../users/";
        String pathHome = "/home/";
        String username = "testUser";
        Path userPathHome = Paths.get(rootUsers + username + pathHome);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(userPathHome)).thenReturn(userPathHome);
            fileElementService.createUserFolder(username);
            mockedFiles.verify(() -> Files.createDirectories(userPathHome));
        }
    }
    @Test
    void createMemberGroupFolder_shouldCreateDirectoryAndReturnFolderPath() throws IOException {
        String rootGroups = "../groups/";
        Integer memberGroupID = 123;
        String groupName = "TestGroup";
        Path groupPath = Paths.get(rootGroups + memberGroupID.toString() + "_" + groupName);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(groupPath)).thenReturn(groupPath);
            String result = fileElementService.createMemberGroupFolder(groupName, memberGroupID);
            mockedFiles.verify(() -> Files.createDirectories(groupPath));
            assertEquals(rootGroups + memberGroupID.toString() + "_" + groupName, result);
        }
    }

    @Test
    void createMemberGroupFolder_shouldHandleIOException() throws IOException {
        String rootGroups = "../groups/";
        Integer memberGroupID = 123;
        String groupName = "TestGroup";
        Path groupPath = Paths.get(rootGroups + memberGroupID.toString() + "_" + groupName);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            IOException expectedException = new IOException("Failed to create directory!");
            mockedFiles.when(() -> Files.createDirectories(groupPath)).thenThrow(expectedException);
            String result = fileElementService.createMemberGroupFolder(groupName, memberGroupID);
            mockedFiles.verify(() -> Files.createDirectories(groupPath));
            assertEquals("Something went wrong with creating a group path!", result);
        }
    }

    @Test
    void createNewFolder_shouldCreateAndReturnParentFolder() {
        // Arrange
        CreateNewFolderRequest createNewFolderRequest = new CreateNewFolderRequest();
        createNewFolderRequest.setFolderName("NewFolder");
        createNewFolderRequest.setParentFolderID(123);
        createNewFolderRequest.setIsShared(false);

        Folder parentFolder = new Folder("ParentFolder", null, false, false);
        Optional<Folder> optionalParentFolder = Optional.of(parentFolder);
        Folder newFolder = new Folder(createNewFolderRequest.getFolderName(), createNewFolderRequest.getParentFolderID(), createNewFolderRequest.getIsShared(), false);

        mock(FolderRepository.class);
        when(folderRepository.findById(createNewFolderRequest.getParentFolderID())).thenReturn(optionalParentFolder);
        when(folderRepository.save(newFolder)).thenReturn(newFolder);

        try (MockedStatic<Files> mockedSystem = mockStatic(Files.class)) {
            Folder result = fileElementService.createNewFolder(createNewFolderRequest);
            assertEquals(parentFolder, result);
            assertEquals(newFolder, parentFolder.getChildren().get(0));
        }
    }

    @Test
    void createNewFolder_shouldReturnNullIfParentFolderNotExists() {
        CreateNewFolderRequest createNewFolderRequest = new CreateNewFolderRequest();
        createNewFolderRequest.setFolderName("NewFolder");
        createNewFolderRequest.setParentFolderID(null);
        createNewFolderRequest.setIsShared(false);
    }
    // @Test
    // void createNewFile_shouldCreateAndReturnParentFolder() {
    //     CreateNewFileRequest createNewFileRequest = new CreateNewFileRequest();
    //     createNewFileRequest.setFileName("NewFile");
    //     createNewFileRequest.setParentFolderID(123);
    //     createNewFileRequest.setIsShared(false);
    //     Folder parentFolder = new Folder("ParentFolder", null, false, false);
    //     Optional<Folder> optionalParentFolder = Optional.of(parentFolder);
    //     MyFile newFile = new MyFile(createNewFileRequest.getFileName(), createNewFileRequest.getParentFolderID(), createNewFileRequest.getIsShared(), true);
    //     newFile.setId(1);
    //     FolderRepository folderRepository = mock(FolderRepository.class);
    //     MyFileRepository myFileRepository = mock(MyFileRepository.class);
    //     when(folderRepository.findById(createNewFileRequest.getParentFolderID())).thenReturn(optionalParentFolder);
    //     when(myFileRepository.save(newFile)).thenReturn(newFile);
    //     ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(outContent));
    //     Folder result = fileElementService.createNewFile(createNewFileRequest);
    //     System.setOut(System.out);
    //     assertEquals(parentFolder, result);
    //     assertEquals(newFile, parentFolder.getChildren().get(0));
    //     String consoleOutput = outContent.toString().trim();
    //     assertTrue(consoleOutput.contains("createNewFile"));
    //     assertTrue(consoleOutput.contains("----------------------------------------------"));
    //     assertTrue(consoleOutput.contains(createNewFileRequest.toString()));
    // }

    //  @Test
    // public void shouldReturnDatabaseGeneratedId() {
    //     long expectedId = 12345L;

    //     TestEntityDAO dao = mock(TestEntityDAO.class);
    //     TestEntity entity = new TestEntity("[MESSAGE]");

    //     doAnswer(invocation -> {
    //         ReflectionTestUtils.setField((TestEntity) invocation.getArgument(0), "id", expectedId);
    //         return null;
    //     }).when(dao).save(entity);

    //     assertThat(someLogicToTest(dao, entity)).isEqualTo(expectedId);
    // }
}


