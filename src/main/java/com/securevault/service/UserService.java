package com.securevault.service;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.UserDTO;
import com.securevault.model.User;
import com.securevault.model.VerificationToken;
import com.securevault.repository.UserRepository;
import com.securevault.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailSender emailSender;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                       VerificationTokenRepository verificationTokenRepository, EmailSender emailSender,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailSender = emailSender;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User register(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
            userDTO.getEmail(),
            passwordEncoder.encode(userDTO.getPassword()),
            userDTO.getFirstName(),
            userDTO.getLastName(),
            false // emailVerified = false initially
        );

        userRepository.save(user);

        // Generate verification token and send an email
        String token = generateVerificationToken(user);
        sendVerificationEmail(user, token);

        return user;
    }

    public String login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email is not verified");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        // Generate and return JWT
        return jwtUtil.generateToken(userDetails);
    }

    public void verifyEmail(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);

        if (verificationTokenOptional.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        VerificationToken verificationToken = verificationTokenOptional.get();
        User user = verificationToken.getUser();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public void resendVerificationToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        // Generate a new token and send it again
        String token = generateVerificationToken(user);
        sendVerificationEmail(user, token);
    }

    private String generateVerificationToken(User user) {
        String token = java.util.UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user, LocalDateTime.now().plusHours(1));
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private void sendVerificationEmail(User user, String token) {
        String subject = "Email Verification";
        String text = "Please verify your email by clicking the link: " + 
                      "http://localhost:8080/api/auth/verify?token=" + token;
        emailSender.sendEmail(user.getEmail(), subject, text);
    }
}
