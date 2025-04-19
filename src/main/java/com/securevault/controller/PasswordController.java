package com.securevault.controller;

import com.securevault.dto.PasswordEntryDTO;
import com.securevault.model.PasswordEntry;
import com.securevault.service.PasswordService;
import jakarta.validation.Valid;

import org.apache.el.stream.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.securevault.model.User; 
import com.securevault.repository.*; 
import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordController {

    private final PasswordService passwordService;
    private final UserRepository userRepo;
  

    public PasswordController(PasswordService passwordService,UserRepository userRepo ) {
        this.passwordService = passwordService;
        this.userRepo=userRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody PasswordEntryDTO dto , @AuthenticationPrincipal UserDetails userDetails) {
        String email =userDetails.getUsername();
         User  user =  userRepo.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));
        PasswordEntry entry = new PasswordEntry(
                dto.getPlatform(),
                dto.getUsername(),
                dto.getPassword(),
                user
                
        );
        // service should associate user by ID
        passwordService.add(entry);
        return ResponseEntity.ok("Added");
    }

    @GetMapping("/me")
    public ResponseEntity<List<PasswordEntry>> all( @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();                         
        User user = userRepo.findByEmail(email)
                                .orElseThrow(()-> new RuntimeException("user not found "));
        List<PasswordEntry> list = passwordService.getAllForUser(user.getId());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        passwordService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
