package com.swa.filter.Services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.swa.filter.Authentication.AuthenticationRequest;
import com.swa.filter.Authentication.AuthenticationResponse;
import com.swa.filter.Authentication.RegisterRequest;
import com.swa.filter.Repository.UserRepository;
import com.swa.filter.mySQLTables.Role;
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
    private final FileService fileService;
            
    
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if(userService.checkIfUserExists(registerRequest.getUsername())){
            return AuthenticationResponse.builder().message("User Exits already!").build();
        }
        else{
            var user = User.builder()
                        .name(registerRequest.getName())
                        .username(registerRequest.getUsername())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .role(Role.USER)
                        .build();
            userRepository.save(user);
            fileService.createPathAndFile(registerRequest.getUsername(), user.getUser_id());
            System.out.println(user.getUser_id());
            // var jwtToken = jwtService.generateToken(user);
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