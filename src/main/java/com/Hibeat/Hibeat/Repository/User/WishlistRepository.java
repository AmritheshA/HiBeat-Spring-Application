package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.Model.User.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {

    Wishlist findByUser(User user);
}
