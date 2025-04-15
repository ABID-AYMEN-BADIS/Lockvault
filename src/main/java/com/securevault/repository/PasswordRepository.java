package com.securevault.repository;

import com.securevault.model.PasswordEntry;
import com.securevault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PasswordRepository extends JpaRepository<PasswordEntry, Long> {
    List<PasswordEntry> findByUser(User user);
}
