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

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173",
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
    
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
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
            System.out.println("Image URL: " + itemRequest.getImageUrl());
            System.out.println("Image URL Length: " + (itemRequest.getImageUrl() != null ? itemRequest.getImageUrl().length() : "null"));
            System.out.println("========================");
            
            // Validate required fields
            if (itemRequest.getItemName() == null || itemRequest.getItemName().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (itemRequest.getDescription() == null || itemRequest.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (itemRequest.getCategory() == null || itemRequest.getCategory().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (itemRequest.getLocation() == null || itemRequest.getLocation().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (itemRequest.getItemType() == null || itemRequest.getItemType().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (itemRequest.getContactInfo() == null || itemRequest.getContactInfo().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Extract user email from the request
            String userEmail = itemRequest.getUserEmail();
            
            ItemResponse item = itemService.createItem(itemRequest, userEmail);
            System.out.println("Item created successfully with ID: " + item.getId());
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            System.err.println("Failed to create item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
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
