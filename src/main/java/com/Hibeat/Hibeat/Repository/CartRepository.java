package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Admin;
import com.Hibeat.Hibeat.Model.Cart;
import com.Hibeat.Hibeat.Model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.ServletRequestBindingException;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserId(int id);

    Cart findByUser(User user);



}
