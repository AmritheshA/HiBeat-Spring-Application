package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Integer>{


    User findByName(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    List<User> findByNameContaining(String keyword);

    User findAllById(Integer productId);
}

