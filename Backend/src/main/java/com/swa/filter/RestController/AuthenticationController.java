package com.swa.filter.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swa.filter.Authentication.AuthenticationRequest;
import com.swa.filter.Authentication.AuthenticationResponse;
import com.swa.filter.Authentication.RegisterRequest;
import com.swa.filter.Services.AuthenticationService;
import com.swa.filter.Services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody RegisterRequest registerRequest){
        if(authenticationService.register(registerRequest)==null){
            return ResponseEntity.badRequest().body("User exists already!");
        }else{
            return ResponseEntity.ok(authenticationService.register(registerRequest));
        }
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));

    }
}
