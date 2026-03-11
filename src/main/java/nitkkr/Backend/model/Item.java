package nitkkr.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_name", nullable = false)
    private String itemName;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    
    @Column(nullable = false)
    private String location;
    
    @Column(name = "item_date", nullable = false)
    private LocalDate itemDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType itemType;
    
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;
    
    @Column(name = "image_url", length = 5000)
    @Lob
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status = ItemStatus.PENDING;
    
    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @PrePersist
    protected void onCreate() {
        postedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
