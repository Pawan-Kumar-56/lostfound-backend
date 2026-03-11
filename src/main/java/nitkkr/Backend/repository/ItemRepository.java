package nitkkr.Backend.repository;

import nitkkr.Backend.model.Item;
import nitkkr.Backend.model.ItemStatus;
import nitkkr.Backend.model.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    Page<Item> findByItemType(ItemType itemType, Pageable pageable);
    
    Page<Item> findByItemTypeAndUserEmail(ItemType itemType, String userEmail, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE " +
           "LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Item> searchItems(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE " +
           "(LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "i.user.email = :userEmail")
    Page<Item> searchItemsByUser(@Param("search") String search, @Param("userEmail") String userEmail, Pageable pageable);
    
    @Query("SELECT i FROM Item i JOIN i.user u WHERE u.email = :userEmail ORDER BY i.postedAt DESC")
    Page<Item> findByUserEmail(@Param("userEmail") String userEmail, Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM Item i JOIN i.user u WHERE u.email = :userEmail AND i.itemType = :itemType")
    long countByUserEmailAndItemType(@Param("userEmail") String userEmail, @Param("itemType") ItemType itemType);
    
    @Query("SELECT COUNT(i) FROM Item i JOIN i.user u WHERE u.email = :userEmail AND i.status = :status")
    long countByUserEmailAndStatus(@Param("userEmail") String userEmail, @Param("status") ItemStatus status);
    
    @Query("SELECT COUNT(i) FROM Item i JOIN i.user u WHERE u.email = :userEmail")
    long countByUserEmail(@Param("userEmail") String userEmail);
}
