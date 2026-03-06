package nitkkr.Backend.controller;

import nitkkr.Backend.dto.ItemRequest;
import nitkkr.Backend.dto.ItemResponse;
import nitkkr.Backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173",
        "http://localhost:5174",
        "https://lostfound-frontend-eight.vercel.app",
        "https://*.vercel.app"
})
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Backend is working! ItemController is accessible.");
    }
    
    @GetMapping("/test-simple")
    public ResponseEntity<String> testSimple() {
        return ResponseEntity.ok("Simple test working - no database involved!");
    }
    
    @GetMapping("/test-db")
    public ResponseEntity<String> testDatabase() {
        try {
            long itemCount = itemService.getTotalItemCount();
            return ResponseEntity.ok("Database connection working! Total items: " + itemCount);
        } catch (Exception e) {
            System.err.println("Database test failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Database test failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponse>> searchItems(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemResponse> items = itemService.searchItems(search, pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/my")
    public ResponseEntity<Page<ItemResponse>> getMyItems(
            @RequestParam String userEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemResponse> items = itemService.getItemsByUser(userEmail, pageable);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        ItemResponse item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
    
    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            System.out.println("=== GET ALL ITEMS DEBUG ===");
            System.out.println("Page: " + page + ", Size: " + size);
            
            Pageable pageable = PageRequest.of(page, size);
            Page<ItemResponse> items = itemService.getAllItems(pageable);
            
            System.out.println("Items loaded successfully: " + items.getTotalElements() + " total items");
            System.out.println("Items on this page: " + items.getNumberOfElements());
            
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            System.err.println("Error in getAllItems: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @PostMapping("/test-create")
    public ResponseEntity<String> testCreateItem(@RequestBody Map<String, Object> testData) {
        try {
            System.out.println("=== TEST CREATE ITEM DEBUG ===");
            System.out.println("Test data received: " + testData);
            System.out.println("Keys: " + testData.keySet());
            System.out.println("==============================");
            return ResponseEntity.ok("Test create endpoint working! Received: " + testData.size() + " fields");
        } catch (Exception e) {
            System.err.println("Error in test create: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Test create failed: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
        System.out.println("=== CREATE ITEM CONTROLLER DEBUG ===");
        System.out.println("POST request received at /api/items");
        System.out.println("ItemRequest object: " + itemRequest);
        System.out.println("====================================");
        
        try {
            System.out.println("=== CREATE ITEM DEBUG ===");
            System.out.println("Item request received");
            System.out.println("Item Name: " + itemRequest.getItemName());
            System.out.println("Description: " + itemRequest.getDescription());
            System.out.println("Category: " + itemRequest.getCategory());
            System.out.println("Location: " + itemRequest.getLocation());
            System.out.println("Item Type: " + itemRequest.getItemType());
            System.out.println("Contact Info: " + itemRequest.getContactInfo());
            System.out.println("User Email: " + itemRequest.getUserEmail());
            System.out.println("User Full Name: " + itemRequest.getUserFullName());
            System.out.println("Image URL: " + (itemRequest.getImageUrl() != null ? "Present" : "Null"));
            System.out.println("Image URL Length: " + (itemRequest.getImageUrl() != null ? itemRequest.getImageUrl().length() : "null"));
            System.out.println("========================");
            
            // Validate required fields
            if (itemRequest.getItemName() == null || itemRequest.getItemName().trim().isEmpty()) {
                throw new RuntimeException("Item name is required");
            }
            if (itemRequest.getDescription() == null || itemRequest.getDescription().trim().isEmpty()) {
                throw new RuntimeException("Description is required");
            }
            if (itemRequest.getCategory() == null || itemRequest.getCategory().trim().isEmpty()) {
                throw new RuntimeException("Category is required");
            }
            if (itemRequest.getLocation() == null || itemRequest.getLocation().trim().isEmpty()) {
                throw new RuntimeException("Location is required");
            }
            if (itemRequest.getItemType() == null || itemRequest.getItemType().trim().isEmpty()) {
                throw new RuntimeException("Item type is required");
            }
            if (itemRequest.getContactInfo() == null || itemRequest.getContactInfo().trim().isEmpty()) {
                throw new RuntimeException("Contact info is required");
            }
            
            // Extract user email from the request
            String userEmail = itemRequest.getUserEmail();
            
            ItemResponse item = itemService.createItem(itemRequest, userEmail);
            System.out.println("Item created successfully with ID: " + item.getId());
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            System.err.println("ERROR IN CREATE ITEM CONTROLLER: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
