package com.securevault.controller;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.UserDTO;
import com.securevault.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO dto) {
        userService.register(dto);
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest req) {
        String token = userService.login(req);
        return ResponseEntity.ok(token);
    }

    /*@GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("Email verified");
    }*/

    /*@PostMapping("/resend-verification")
    public ResponseEntity<String> resend(@RequestParam String email) {
        userService.resendVerificationToken(email);
        return ResponseEntity.ok("Token resent");
    }*/
}
