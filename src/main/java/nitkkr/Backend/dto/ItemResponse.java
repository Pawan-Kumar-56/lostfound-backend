package nitkkr.Backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemResponse {
    
    private Long id;
    private String itemName;
    private String description;
    private String category;
    private String location;
    private LocalDate itemDate;
    private String itemType;
    private String contactInfo;
    private String imageUrl;
    private String status;
    private String postedBy;
    private String postedByEmail; // Add user email for filtering
    private LocalDateTime postedAt;
    private LocalDateTime resolvedAt;
}
