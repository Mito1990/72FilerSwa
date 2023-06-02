package com.swa.filter.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swa.filter.ObjectModel.AuthenticationRequest;
import com.swa.filter.ObjectModel.AuthenticationResponse;
import com.swa.filter.ObjectModel.RegisterRequest;
import com.swa.filter.ObjectModel.Role;
import com.swa.filter.Repository.HomeDirRepository;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.FolderDir;
import com.swa.filter.mySQLTables.HomeDir;
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
    private final HomeDirRepository homeDirRepository;
    private final FileService fileService;
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if(userService.checkIfUserExists(registerRequest.getUsername())){
            return AuthenticationResponse.builder().message("User Exits already!").build();
        }
        else{
            var homeS = HomeDir.builder()
                .name("root")
                .date(new Date())
                .path(fileService.createUserFolder(registerRequest.getUsername()))
                .build();
            homeDirRepository.save(homeS);
            var user = User.builder()
                        .name(registerRequest.getName())
                        .username(registerRequest.getUsername())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .role(Role.USER)
                        .home(homeS)
                        .build();
            userRepository.save(user);
            return AuthenticationResponse.builder().message("User succsesfull registered!").build();

        }

    }
    

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        var user = userRepository.findUserByUsername(authenticationRequest.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().message(jwtToken).build();
    }
    
}