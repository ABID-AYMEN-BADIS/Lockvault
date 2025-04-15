package com.securevault.service;

import com.securevault.model.PasswordEntry;
import com.securevault.repository.PasswordRepository;
import com.securevault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository, UserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
    }

    public void addPasswordEntry(Long userId, String platform, String username, String password) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PasswordEntry passwordEntry = new PasswordEntry(platform, username, password, user);
        passwordRepository.save(passwordEntry);
    }

    public List<PasswordEntry> getAllPasswords(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return passwordRepository.findByUser(user);
    }

    public void deletePasswordEntry(Long passwordId) {
        passwordRepository.deleteById(passwordId);
    }
}
