package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.Cart;
import com.Hibeat.Hibeat.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserId(int id);

    Cart findByUser(User user);



}
