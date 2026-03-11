package nitkkr.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequest {
    
    @NotBlank(message = "Item name is required")
    @Size(max = 200, message = "Item name must not exceed 200 characters")
    private String itemName;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private String category;
    
    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;
    
    @NotNull(message = "Item date is required")
    private LocalDate itemDate;
    
    @NotNull(message = "Item type is required")
    private String itemType;
    
    @NotBlank(message = "Contact information is required")
    @Size(max = 200, message = "Contact information must not exceed 200 characters")
    private String contactInfo;
    
    private String imageUrl;
    
    private String userEmail;
    
    private String userFullName;
}
