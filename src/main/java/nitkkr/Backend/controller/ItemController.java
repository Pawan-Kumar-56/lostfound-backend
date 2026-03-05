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
    
    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemResponse> items = itemService.getAllItems(pageable);
        return ResponseEntity.ok(items);
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
    
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
        try {
            // Extract user email from the request (for now, from the request body)
            String userEmail = itemRequest.getUserEmail();
            
            ItemResponse item = itemService.createItem(itemRequest, userEmail);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            System.err.println("Failed to create item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        try {
            ItemResponse item = itemService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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
