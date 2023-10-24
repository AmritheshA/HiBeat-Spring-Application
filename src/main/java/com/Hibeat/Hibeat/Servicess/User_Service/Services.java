package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Services {

    User save_user(DTO userInfo);

    User findByName(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    User findAllById(Integer productId);


    List<User> findByNameContaining(String keyword);

    Cart save_cart(Cart productIds);


    Orders save_orders(Orders orders);

    List<Products> findAllByIsdIn(List<Integer> productIds);

    public List<Orders> findByUser(User user);

}
