package nitkkr.Backend.service;

import nitkkr.Backend.dto.ItemRequest;
import nitkkr.Backend.dto.ItemResponse;
import nitkkr.Backend.model.Item;
import nitkkr.Backend.model.User;
import nitkkr.Backend.model.Category;
import nitkkr.Backend.model.ItemType;
import nitkkr.Backend.repository.ItemRepository;
import nitkkr.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<ItemResponse> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(this::convertToResponse);
    }

    public long getTotalItemCount() {
        return itemRepository.count();
    }

    public Page<ItemResponse> searchItems(String search, Pageable pageable) {
        return itemRepository.searchItems(search, pageable).map(this::convertToResponse);
    }

    public Page<ItemResponse> getItemsByUser(String userEmail, Pageable pageable) {
        return itemRepository.findByUserEmail(userEmail, pageable).map(this::convertToResponse);
    }

    public ItemResponse createItem(ItemRequest itemRequest, String userEmail) {
        try {
            System.out.println("=== CREATE ITEM DEBUG ===");
            System.out.println("Item request received");
            System.out.println("Item Name: " + itemRequest.getItemName());
            System.out.println("Description: " + itemRequest.getDescription());
            System.out.println("Category: " + itemRequest.getCategory());
            System.out.println("Location: " + itemRequest.getLocation());
            System.out.println("Item Type: " + itemRequest.getItemType());
            System.out.println("Contact Info: " + itemRequest.getContactInfo());
            System.out.println("User Email: " + userEmail);
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

            // Find or create user based on provided email
            User user = userRepository.findByEmail(userEmail)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(userEmail);
                        // Use the provided full name or extract from email as fallback
                        String fullName = itemRequest.getUserFullName();
                        if (fullName == null || fullName.trim().isEmpty()) {
                            // Extract name from email for now (before the @ symbol)
                            String name = userEmail.substring(0, userEmail.indexOf("@"));
                            fullName = name.substring(0, 1).toUpperCase() + name.substring(1);
                        }
                        newUser.setFullName(fullName);
                        newUser.setPassword("password");
                        newUser.setCreatedAt(LocalDateTime.now());
                        newUser.setUpdatedAt(LocalDateTime.now());
                        newUser.setIsEmailVerified(true);
                        return userRepository.save(newUser);
                    });

            Item item = new Item();
            item.setItemName(itemRequest.getItemName());
            item.setDescription(itemRequest.getDescription());

            // Handle category conversion with error handling
            try {
                Category category = Category.valueOf(itemRequest.getCategory().toUpperCase());
                item.setCategory(category);
                System.out.println("Category set successfully: " + category);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid category: " + itemRequest.getCategory());
                System.err.println("Available categories: " + java.util.Arrays.toString(Category.values()));
                throw new RuntimeException("Invalid category: " + itemRequest.getCategory() + ". Available categories: " + java.util.Arrays.toString(Category.values()));
            }

            item.setLocation(itemRequest.getLocation());
            item.setItemDate(itemRequest.getItemDate());

            // Handle item type conversion with error handling
            try {
                ItemType itemType = ItemType.valueOf(itemRequest.getItemType().toUpperCase());
                item.setItemType(itemType);
                System.out.println("Item type set successfully: " + itemType);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid item type: " + itemRequest.getItemType());
                System.err.println("Available types: " + java.util.Arrays.toString(ItemType.values()));
                throw new RuntimeException("Invalid item type: " + itemRequest.getItemType() + ". Available types: " + java.util.Arrays.toString(ItemType.values()));
            }

            item.setContactInfo(itemRequest.getContactInfo());
            item.setImageUrl(itemRequest.getImageUrl() != null && !itemRequest.getImageUrl().isEmpty() ? itemRequest.getImageUrl() : null);

            System.out.println("=== ITEM SET DEBUG ===");
            System.out.println("Setting ImageUrl: " + itemRequest.getImageUrl());
            System.out.println("Final Item ImageUrl: " + item.getImageUrl());
            System.out.println("======================");

            item.setUser(user);
            item.setPostedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
            System.out.println("Item saved successfully with ID: " + savedItem.getId());
            return convertToResponse(savedItem);
        } catch (Exception e) {
            System.err.println("ERROR IN CREATE ITEM: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create item: " + e.getMessage());
        }
    }

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return convertToResponse(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    private ItemResponse convertToResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setItemName(item.getItemName());
        response.setDescription(item.getDescription());
        response.setCategory(item.getCategory().name());
        response.setLocation(item.getLocation());
        response.setItemDate(item.getItemDate());
        response.setItemType(item.getItemType().name());
        response.setContactInfo(item.getContactInfo());
        response.setImageUrl(item.getImageUrl());
        response.setStatus(item.getStatus().name());

        // Safe user access with null checks
        if (item.getUser() != null) {
            System.err.println("WARNING: Item has no user associated - ID: " + item.getId());
            response.setPostedBy("Unknown User");
            response.setPostedByEmail("unknown@example.com");
        } else {
            response.setPostedBy(item.getUser().getFullName());
            response.setPostedByEmail(item.getUser().getEmail());
        }

        response.setPostedAt(item.getPostedAt());
        response.setResolvedAt(item.getResolvedAt());

        System.out.println("=== ITEM RESPONSE DEBUG ===");
        System.out.println("PostedBy: " + response.getPostedBy());
        System.out.println("PostedByEmail: " + response.getPostedByEmail());
        System.out.println("ItemID: " + response.getId());
        System.out.println("ImageUrl: " + response.getImageUrl());
        System.out.println("ImageUrl Length: " + (response.getImageUrl() != null ? response.getImageUrl().length() : "null"));
        System.out.println("===============================");

        return response;
    }
}