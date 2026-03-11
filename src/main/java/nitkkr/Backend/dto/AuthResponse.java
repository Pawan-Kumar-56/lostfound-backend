package nitkkr.Backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    
    private String token;
    private String refreshToken;
    private Long userId;
    private String email;
    private String fullName;
    private String role;
    
    public AuthResponse(String token, String refreshToken, Long userId, String email, String fullName, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
}
