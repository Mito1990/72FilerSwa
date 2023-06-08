package com.swa.filter.Services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.swa.filter.ObjectModel.AuthenticationRequest;
import com.swa.filter.ObjectModel.AuthenticationResponse;
import com.swa.filter.ObjectModel.RegisterRequest;
import com.swa.filter.ObjectModel.Role;
import com.swa.filter.Repository.FolderRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.Folder;
import com.swa.filter.mySQLTables.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final FileElementService fileElementService;
    private final FolderRepository folderRepository;
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if(userService.checkIfUserExists(registerRequest.getUsername())){
            return AuthenticationResponse.builder().message("User exists already!").userExists(true).build();
        }else{
            Folder home = new Folder("home", null, false, false);
            folderRepository.save(home);
            var user = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .home(home)
                .memberGroups(null)
                .build();
            fileElementService.createUserFolder(registerRequest.getUsername());
            userRepository.save(user);
            return AuthenticationResponse.builder().message("User successful registered!").userExists(false).build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        var user = userRepository.findUserByUsername(authenticationRequest.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().message(jwtToken).homeID(user.getHome().getId()).build();
    }
}