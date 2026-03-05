package nitkkr.Backend.service;

import nitkkr.Backend.dto.AuthRequest;
import nitkkr.Backend.dto.AuthResponse;
import nitkkr.Backend.model.Role;
import nitkkr.Backend.model.User;
import nitkkr.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AuthResponse register(AuthRequest authRequest) {
        System.out.println("Registering user: " + authRequest.getEmail());
        
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            System.out.println("Email already exists: " + authRequest.getEmail());
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setFullName(authRequest.getFullName() != null ? authRequest.getFullName() : "Test User");
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.USER);
        user.setIsEmailVerified(true);
        user.setContactNumber(authRequest.getContactNumber());
        user.setDepartment(authRequest.getDepartment());
        user.setYear(authRequest.getYear());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        try {
            User savedUser = userRepository.save(user);
            System.out.println("User saved successfully: " + savedUser.getId());
            System.out.println("Full name: " + savedUser.getFullName());
            System.out.println("Contact: " + savedUser.getContactNumber());
            System.out.println("Department: " + savedUser.getDepartment());
            System.out.println("Year: " + savedUser.getYear());
            
            return new AuthResponse(
                "test-jwt-token-" + savedUser.getId(),
                "refresh-token-" + savedUser.getId(),
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().name()
            );
        } catch (Exception e) {
            System.err.println("Failed to save user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }
    
    public AuthResponse login(AuthRequest authRequest) {
        System.out.println("Login attempt: " + authRequest.getEmail());
        
        User user = userRepository.findByEmail(authRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        System.out.println("Login successful for: " + user.getEmail());
        
        return new AuthResponse(
            "test-jwt-token-" + user.getId(),
            "refresh-token-" + user.getId(),
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getRole().name()
        );
    }
}
