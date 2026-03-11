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
    
    @GetMapping("/test-items-data")
    public ResponseEntity<Map<String, Object>> testItemsData() {
        try {
            // Return hardcoded test data to verify frontend works
            Map<String, Object> testResponse = new java.util.HashMap<>();
            
            // Create test items list
            java.util.List<Map<String, Object>> testItems = new java.util.ArrayList<>();
            
            Map<String, Object> item1 = new java.util.HashMap<>();
            item1.put("id", 1L);
            item1.put("itemName", "Test Item 1");
            item1.put("description", "This is a test item");
            item1.put("category", "ELECTRONICS");
            item1.put("location", "Test Location");
            item1.put("itemDate", "2024-01-01");
            item1.put("itemType", "LOST");
            item1.put("contactInfo", "test@example.com");
            item1.put("imageUrl", "https://picsum.photos/300/200?random=1");
            item1.put("postedBy", "Test User");
            item1.put("postedAt", "2024-01-01T10:00:00");
            
            Map<String, Object> item2 = new java.util.HashMap<>();
            item2.put("id", 2L);
            item2.put("itemName", "Test Item 2");
            item2.put("description", "Another test item");
            item2.put("category", "DOCUMENTS");
            item2.put("location", "Test Location 2");
            item2.put("itemDate", "2024-01-02");
            item2.put("itemType", "FOUND");
            item2.put("contactInfo", "test2@example.com");
            item2.put("imageUrl", "https://picsum.photos/300/200?random=2");
            item2.put("postedBy", "Test User 2");
            item2.put("postedAt", "2024-01-02T10:00:00");
            
            testItems.add(item1);
            testItems.add(item2);
            
            testResponse.put("content", testItems);
            testResponse.put("totalElements", 2);
            testResponse.put("totalPages", 1);
            testResponse.put("numberOfElements", 2);
            testResponse.put("first", true);
            testResponse.put("last", true);
            
            System.out.println("=== TEST ITEMS DATA ===");
            System.out.println("Returning test data with 2 items");
            System.out.println("=======================");
            
            return ResponseEntity.ok(testResponse);
        } catch (Exception e) {
            System.err.println("Error in test items data: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("error", "Failed to generate test data");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    @GetMapping("/simple-items")
    public ResponseEntity<java.util.List<Map<String, Object>>> getSimpleItems() {
        try {
            java.util.List<Map<String, Object>> simpleItems = new java.util.ArrayList<>();
            
            // Create a simple test item
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("id", 1L);
            item.put("itemName", "Test Lost Item");
            item.put("description", "This is a test item that should appear in ViewItem");
            item.put("category", "ELECTRONICS");
            item.put("location", "Library");
            item.put("itemDate", "2024-03-10");
            item.put("itemType", "lost");
            item.put("contactInfo", "test@example.com");
            item.put("imageUrl", "https://picsum.photos/300/200?random=1");
            item.put("postedBy", "Test User");
            item.put("postedAt", "2024-03-10T10:00:00");
            
            simpleItems.add(item);
            
            System.out.println("=== SIMPLE ITEMS DEBUG ===");
            System.out.println("Returning simple items list with " + simpleItems.size() + " items");
            System.out.println("First item: " + simpleItems.get(0));
            System.out.println("========================");
            
            return ResponseEntity.ok(simpleItems);
        } catch (Exception e) {
            System.err.println("Error in simple items: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/debug-items")
    public ResponseEntity<String> debugItems() {
        try {
            long totalItems = itemService.getTotalItemCount();
            return ResponseEntity.ok("Total items in database: " + totalItems);
        } catch (Exception e) {
            System.err.println("Debug items error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
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
            
            // Log the actual response structure
            System.out.println("Response content size: " + items.getContent().size());
            if (items.hasContent()) {
                System.out.println("First item: " + items.getContent().get(0));
            }
            System.out.println("===============================");
            
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
