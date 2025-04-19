package com.securevault.service;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.UserDTO;
import com.securevault.model.User;
import com.securevault.model.VerificationToken;
import com.securevault.repository.UserRepository;
import com.securevault.repository.VerificationTokenRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepo;
    // private final EmailSender emailSender; // Commenté pour désactiver l'envoi d'email
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository tokenRepo,
                       // EmailSender emailSender, // Commenté pour désactiver l'envoi d'email
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
        // this.emailSender = emailSender; // Commenté pour désactiver l'envoi d'email
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User register(UserDTO dto) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> { throw new RuntimeException("Email already exists"); });

        User user = new User(
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getFirstName(),
                dto.getLastName(),
                false
        );
        userRepository.save(user);

        /*String token = createVerificationToken(user);*/
        // sendVerificationEmail(user, token); // Commenté pour désactiver l'envoi d'email
        return user;
    }

    public String login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        /*if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }*/
        UserDetails ud = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        return jwtUtil.generateToken(ud);
    }

    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
    
    public UserDetails getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return loadUserByUsername(username);
    } 
    /*@Transactional
    public void verifyEmail(String token) {
        VerificationToken vt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        User user = vt.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
    }*/

    /*public void resendVerificationToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }
        VerificationToken old = tokenRepo.findByToken(email).orElse(null);
        // optional: delete old token
        String token = createVerificationToken(user);
        // sendVerificationEmail(user, token); // Commenté pour désactiver l'envoi d'email
    }

    private String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken(token, user, LocalDateTime.now().plusHours(1));
        tokenRepo.save(vt);
        return token;
    }

    // private void sendVerificationEmail(User user, String token) { // Commenté pour désactiver l'envoi d'email
    //     String link = "http://localhost:8080/api/auth/verify?token=" + token;
    //     emailSender.sendEmail(user.getEmail(), "Verify your email", "Click: " + link);
    // }*/

}
