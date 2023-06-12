package com.swa.filter.Testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.swa.filter.ObjectModel.CreateNewFileRequest;
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

class FileElementServiceCreateNewFileTests{
    // @Mock
    // private FileElementService fileElementService;
    @Mock
    private UserRepository userRepository;
    // @Mock
    // private JwtService jwtService;
    // @Mock
    // private FolderRepository folderRepository;
    @Mock
    private MemberGroupRepository memberGroupRepository;
    // @Mock
    // private MyFileRepository myFileRepository;
    @Mock
    private FileElementRepository fileElementRepository;

    @Test
    void createNewFile_shouldCreateAndReturnParentFolder() throws IOException {
        CreateNewFileRequest createNewFileRequest = new CreateNewFileRequest();
        createNewFileRequest.setFileName("NewFile");
        createNewFileRequest.setParentFolderID(123);
        createNewFileRequest.setIsShared(false);
        createNewFileRequest.setToken("sampleToken");
    
        Folder parentFolder = new Folder("ParentFolder", null, false, false);
        Optional<Folder> optionalParentFolder = Optional.of(parentFolder);
        MyFile newFile = new MyFile(createNewFileRequest.getFileName(), createNewFileRequest.getParentFolderID(), createNewFileRequest.getIsShared(), true);
        newFile.setId(12345);
        FolderRepository folderRepository = mock(FolderRepository.class);

        
        MyFileRepository myFileRepository = mock(MyFileRepository.class);
        JwtService jwtService = mock(JwtService.class);
    
        when(folderRepository.findById(createNewFileRequest.getParentFolderID())).thenReturn(optionalParentFolder);
        when(myFileRepository.save(any(MyFile.class))).thenAnswer(
            invocation -> {
            Object[] args = invocation.getArguments();
            ((MyFile)args[0]).setId(12345);
            return null;
        });
        when(jwtService.extractUsername(createNewFileRequest.getToken())).thenReturn("sampleOwner");

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    
        FileElementService fileElementService = new FileElementService(
                userRepository,
                jwtService,
                folderRepository,
                memberGroupRepository,
                myFileRepository,
                fileElementRepository
        );

        // Act
        Folder result = fileElementService.createNewFile(createNewFileRequest);
    
        // Restore the original System.out
        System.setOut(System.out);
    
        // Assert
        assertEquals(parentFolder, result);
        assertEquals(newFile, parentFolder.getChildren().get(0));
    
        // Verify console output
        String consoleOutput = outContent.toString().trim();
        assertTrue(consoleOutput.contains("createNewFile"));
        assertTrue(consoleOutput.contains("----------------------------------------------"));
        assertTrue(consoleOutput.contains(createNewFileRequest.toString()));
        assertTrue(consoleOutput.contains("File created successfully."));

        // Verify that the file path is set correctly
        assertNull(newFile.getPath()); // Assuming the file path is set in the createNewFile method
        assertEquals(12345, newFile.getId());
    }
}



