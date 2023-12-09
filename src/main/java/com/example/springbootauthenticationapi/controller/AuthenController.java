package com.example.springbootauthenticationapi.controller;

import com.example.springbootauthenticationapi.entity.User;
import com.example.springbootauthenticationapi.response.JwtResponse;
import com.example.springbootauthenticationapi.service.UserService;
import com.example.springbootauthenticationapi.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class AuthenController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token  = jwtService.generateToken(authentication);
            return ResponseEntity.ok().body(new JwtResponse(user.getUsername(),token));

        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return ResponseEntity.ok().body("Username or password not correct");
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        try {
            if(userService.findUserByUsername(user.getUsername()) != null) {
                return "username is exists, please choose an other username";
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.createAccount(user);
            return "create account success";

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "register not success";
    }


}