package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {

    Wishlist findByUser(User user);
}
