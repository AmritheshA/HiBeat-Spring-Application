package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.User.Review;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ReviewDTO;
import com.Hibeat.Hibeat.Servicess.User_Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/write-review")
    public ResponseEntity<String> writeReview(@ModelAttribute ReviewDTO reviews,
                                      @RequestParam("image")MultipartFile image){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication.isAuthenticated())){
            return ResponseEntity.ok().body("failed");

        }
        return reviewService.writeReview(reviews,image);
    }
    @PostMapping("/edit-review")
    public String editReview(@ModelAttribute ReviewDTO reviews){
        return reviewService.editReview(reviews);
    }

    @GetMapping("/get-review")
    public ResponseEntity<String[]> getReviews(@RequestParam("id") int id){
        Review review = reviewService.getReview(id);
        String[] reviews = {review.getReviewTitle(),review.getReview(),String.valueOf(review.getRating()),review.getImage()};
        return ResponseEntity.ok(reviews);
    }
}


