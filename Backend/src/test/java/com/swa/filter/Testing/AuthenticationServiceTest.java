package com.swa.filter.Testing;
import com.swa.filter.ObjectModel.AuthenticationRequest;
import com.swa.filter.ObjectModel.AuthenticationResponse;
import com.swa.filter.ObjectModel.RegisterRequest;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.Services.AuthenticationService;
import com.swa.filter.Services.FileElementService;
import com.swa.filter.Services.JwtService;
import com.swa.filter.Services.UserService;
import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.User;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private FileElementService fileElementService;

    @Mock
    private FolderRepository folderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                userService,
                fileElementService,
                folderRepository
        );
    }

    @Test
    void testRegister_UserDoesNotExist_SuccessfulRegistration() {
        RegisterRequest registerRequest = new RegisterRequest("John", "john123", "password");
        when(userService.checkIfUserExists(registerRequest.getUsername())).thenReturn(false);
        when(folderRepository.save(any(Folder.class))).thenReturn(new Folder("home", null, false, false));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        AuthenticationResponse response = authenticationService.register(registerRequest);
        assertFalse(response.getUserExists());
        assertEquals("User successful registered!", response.getMessage());
        verify(userService).checkIfUserExists(registerRequest.getUsername());
        verify(folderRepository).save(any(Folder.class));
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(fileElementService).createUserFolder(registerRequest.getUsername());
    }

    @Test
    void testRegister_UserExists_AlreadyExistsMessage() {
        RegisterRequest registerRequest = new RegisterRequest("John", "john123", "password");
        when(userService.checkIfUserExists(registerRequest.getUsername())).thenReturn(true);
        AuthenticationResponse response = authenticationService.register(registerRequest);
        assertTrue(response.getUserExists());
        assertEquals("User exists already!", response.getMessage());
        verify(userService).checkIfUserExists(registerRequest.getUsername());
        verifyNoMoreInteractions(folderRepository, passwordEncoder, userRepository, fileElementService);
    }

    @Test
    void testAuthenticate_ValidCredentials_SuccessfulAuthentication() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john123", "password");
        Folder folder = new Folder();
        User user = new User();
        user.setHome(folder);
        String jwtToken = "mockJwtToken";
        when(userRepository.findUserByUsername(authenticationRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        assertTrue(response.getUserExists());
        assertEquals(jwtToken, response.getMessage());
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findUserByUsername(authenticationRequest.getUsername());
        verify(jwtService).generateToken(user);
    }
}
