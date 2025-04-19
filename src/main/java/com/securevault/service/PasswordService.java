package com.securevault.service;

import com.securevault.model.PasswordEntry;
import com.securevault.repository.PasswordRepository;
import com.securevault.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PasswordService {

    private final PasswordRepository passwordRepo;
    private final UserRepository userRepo;

    public PasswordService(PasswordRepository passwordRepo, UserRepository userRepo) {
        this.passwordRepo = passwordRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void add(PasswordEntry entry) {
        passwordRepo.save(entry);
    }

    public List<PasswordEntry> getAllForUser(Long userId) {
        return userRepo.findById(userId)
                .map(passwordRepo::findByUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Long entryId) {
        passwordRepo.deleteById(entryId);
    }
}
