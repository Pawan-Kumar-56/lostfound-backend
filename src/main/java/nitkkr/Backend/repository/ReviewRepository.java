package nitkkr.Backend.repository;

import nitkkr.Backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByRating(int rating);
    
    List<Review> findByDepartment(String department);
    
    @Query("SELECT r FROM Review r WHERE r.name LIKE %:name%")
    List<Review> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(r) FROM Review r")
    long countAllReviews();
    
    @Query("SELECT AVG(r.rating) FROM Review r")
    Double getAverageRating();
}
