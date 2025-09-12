package com.cts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.model.AuthRequest;
import com.cts.model.User;
import com.cts.service.AuthService;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private AuthService authService;

	@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
 
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        authService.registerUser(user);
        return ResponseEntity.ok("Registration Is Done!!");
    }
}
