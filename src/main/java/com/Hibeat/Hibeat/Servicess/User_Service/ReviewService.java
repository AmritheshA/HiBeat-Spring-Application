package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.User.Review;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    ResponseEntity<String> writeReview(ReviewDTO reviewDTO, MultipartFile image);

    Review saveReview(Review review);

    List<Review> findByUser(User user);

    Review getReview(int id);

    String editReview(ReviewDTO reviews);

}
