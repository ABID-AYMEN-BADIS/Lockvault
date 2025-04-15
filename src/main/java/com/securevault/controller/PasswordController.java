package com.securevault.controller;

import com.securevault.model.PasswordEntry;
import com.securevault.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPassword(@RequestParam Long userId, 
                                              @RequestParam String platform, 
                                              @RequestParam String username, 
                                              @RequestParam String password) {
        try {
            passwordService.addPasswordEntry(userId, platform, username, password);
            return ResponseEntity.ok("Password entry added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PasswordEntry>> getAllPasswords(@PathVariable Long userId) {
        try {
            var passwords = passwordService.getAllPasswords(userId);
            return ResponseEntity.ok(passwords);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("/delete/{passwordId}")
    public ResponseEntity<String> deletePassword(@PathVariable Long passwordId) {
        try {
            passwordService.deletePasswordEntry(passwordId);
            return ResponseEntity.ok("Password entry deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
