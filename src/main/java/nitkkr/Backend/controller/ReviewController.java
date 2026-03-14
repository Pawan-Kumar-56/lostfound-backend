package nitkkr.Backend.controller;

import nitkkr.Backend.model.Review;
import nitkkr.Backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Review postReview(@RequestBody Review review){
        return reviewService.saveReview(review);
    }
}
