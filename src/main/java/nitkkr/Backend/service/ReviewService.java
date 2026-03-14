package nitkkr.Backend.service;

import nitkkr.Backend.model.Review;
import nitkkr.Backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review){
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> getReviewsByRating(int rating) {
        return reviewRepository.findByRating(rating);
    }

    public List<Review> getReviewsByDepartment(String department) {
        return reviewRepository.findByDepartment(department);
    }
}
