package nitkkr.Backend.controller;

import nitkkr.Backend.dto.AuthRequest;
import nitkkr.Backend.dto.AuthResponse;
import nitkkr.Backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173",
        "http://localhost:5174",
        "https://lostfound-frontend-eight.vercel.app",
        "https://*.vercel.app"
})
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Backend is working! AuthController is accessible.");
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("=== REGISTRATION DEBUG ===");
            System.out.println("Registration request received");
            System.out.println("Email: " + authRequest.getEmail());
            System.out.println("Password: " + (authRequest.getPassword() != null ? "Present" : "Missing"));
            System.out.println("Full Name: " + authRequest.getFullName());
            System.out.println("========================");
            
            // Validate required fields
            if (authRequest.getEmail() == null || authRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }
            if (authRequest.getPassword() == null || authRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
            }
            if (authRequest.getFullName() == null || authRequest.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Full name is required"));
            }
            
            AuthResponse response = authService.register(authRequest);
            System.out.println("Registration successful for: " + authRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Unexpected registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Login request: " + authRequest.getEmail());
            AuthResponse response = authService.login(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
