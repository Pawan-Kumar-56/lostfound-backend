package nitkkr.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class HealthController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            Connection connection = dataSource.getConnection();
            String dbUrl = connection.getMetaData().getURL();
            String dbUser = connection.getMetaData().getUserName();
            connection.close();
            
            return ResponseEntity.ok("✅ Database connected: " + dbUrl + " | User: " + dbUser);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("❌ Database connection failed: " + e.getMessage());
        }
    }
}
