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

        User user = new User();

        user.setFullName(authRequest.getFullName());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                "token",
                "refresh",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().name()
        );

    }

    public AuthResponse login(AuthRequest authRequest) {

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

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