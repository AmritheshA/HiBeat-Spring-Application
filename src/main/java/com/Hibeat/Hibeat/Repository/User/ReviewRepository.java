package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.Review;
import com.Hibeat.Hibeat.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {

    List<Review> findByProducts(Products product);

    List<Review> findByUser(User user);
    @Query("SELECT r.rating FROM Review r WHERE r.products = :product")
    ArrayList<Integer> findAllRating(@Param("product") Products product);

    Review findAllById(int reviewId);
}
