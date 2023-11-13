package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.User.Review;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ReviewDTO;
import com.Hibeat.Hibeat.Repository.User.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserServices userServices;
    private final ProductService productService;

    @Autowired
    public ReviewServiceImp(ReviewRepository reviewRepository, UserServices userServices, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.userServices = userServices;
        this.productService = productService;
    }

    @Override
    public ResponseEntity<String> writeReview(ReviewDTO reviewDTO, MultipartFile image) {

        try {
            List<Review> reviews = findByUser(userServices.currentUser());

            if (reviews.stream().anyMatch(review -> review.getProducts().getId().equals(reviewDTO.getProductId()))) {
//                Review already there
                return ResponseEntity.ok().body("exist");
            }

            Review review = new Review();
            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

            review.setReviewTitle(reviewDTO.getReviewTitle());

            review.setUser(userServices.currentUser());
            review.setProducts(productService.findAllById(reviewDTO.getProductId()));
            review.setReview(reviewDTO.getReview());
            review.setRating(reviewDTO.getRating());
            review.setImage(image.getOriginalFilename());
            image.transferTo(new File(file + image.getOriginalFilename()));
            review.setReviewTime(dateFinder(0));

            saveReview(review);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.info("writeReview, " + e.getMessage());
            return ResponseEntity.ok().body("failed");
        }
    }

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    @Override
    public ResponseEntity<String> saveReplay(String replay, int reviewId) {
        try {
            Optional<Review> review1 = reviewRepository.findById(reviewId);
            if (review1.isPresent()) {
                Review review = review1.get();
                if(review.getUser().equals(userServices.currentUser())){
                    return ResponseEntity.ok().body("null");
                }
//                List<String> replays = review.getReplays();
//                replays.add(replay);
                saveReview(review);
            }
            return ResponseEntity.ok().body(userServices.currentUserName());
        } catch (Exception e) {
            log.info("saveReplay  " + e.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }

    @Override
    public Review findReview(Integer reviewId) {
        return reviewRepository.findAllById(reviewId);
    }

    @Override
    public Review getReview(int id) {
        return reviewRepository.findAllById(id);
    }

    @Override
    public String editReview(ReviewDTO reviews) {
        try {
            List<Review> review = reviewRepository.findByProducts(productService.findAllById(reviews.getProductId()));

            Review editReview = review.stream().filter(review1 -> review1.getUser().equals(userServices.currentUser())).findFirst().get();

            editReview.setReviewTitle(reviews.getReviewTitle());
            editReview.setReview(reviews.getReview());
            editReview.setRating(reviews.getRating());

            editReview.setReviewTime(dateFinder(0));

            saveReview(editReview);
            return "redirect:/user/product-details/"+reviews.getProductId();
        } catch (Exception e) {
            log.info("writeReview, " + e.getMessage());
            return "Exception/404";
        }
    }


    public LocalDate dateFinder(int numOfDates) {
        ZonedDateTime kolkataTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime date = kolkataTime.plusDays(numOfDates);

        return date.toLocalDate();
    }

}
