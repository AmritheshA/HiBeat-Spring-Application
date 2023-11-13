package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Integer>{


    User findByName(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    List<User> findByNameContaining(String keyword);

    User findAllById(Integer productId);

    @Query("select COUNT(*) from User ")
    Integer totalUserCount();


}

