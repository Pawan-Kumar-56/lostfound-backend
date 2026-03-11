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
import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // In-memory OTP storage (for development - use Redis in production)
    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    // Generate and send OTP
    public String generateAndSendOtp(String email) {
        // Generate 6-digit OTP
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));

        // Store OTP with 5-minute expiry
        otpStorage.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5));

        System.out.println("=== OTP DEBUG ===");
        System.out.println("Email: " + email);
        System.out.println("Generated OTP: " + otp);
        System.out.println("Expires at: " + otpExpiry.get(email));
        System.out.println("================");

        try {
            // Send real email OTP using Brevo
            emailService.sendOtp(email, otp);
            System.out.println("✅ OTP sent successfully via Brevo to: " + email);
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            // Still return OTP for development if email fails
            System.out.println("=== DEVELOPMENT FALLBACK ===");
            System.out.println("Email failed, but OTP is: " + otp);
            System.out.println("Use this OTP to continue registration");
            System.out.println("==========================");
        }

        return otp; // Return OTP for development fallback
    }

    // Verify OTP
    public boolean verifyOtp(String email, String inputOtp) {
        String storedOtp = otpStorage.get(email);
        LocalDateTime expiry = otpExpiry.get(email);

        if (storedOtp == null || expiry == null) {
            System.out.println("OTP not found for email: " + email);
            return false;
        }

        if (LocalDateTime.now().isAfter(expiry)) {
            System.out.println("OTP expired for email: " + email);
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }

        boolean isValid = storedOtp.equals(inputOtp);
        System.out.println("OTP verification for " + email + ": " + (isValid ? "SUCCESS" : "FAILED"));

        if (isValid) {
            // Clear OTP after successful verification
            otpStorage.remove(email);
            otpExpiry.remove(email);
        }

        return isValid;
    }

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