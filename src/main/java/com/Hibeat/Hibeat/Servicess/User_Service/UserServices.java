package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Banner;
import com.Hibeat.Hibeat.Model.User.Cart;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public interface UserServices {

    User saveUser(DTO userInfo);

    User save(User user);

    User findByName(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    User findAllById(Integer productId);

    List<User> findByNameContaining(String keyword);

    Cart save_cart(Cart productIds);

    Orders save_orders(Orders orders);

//    List<Products> findAllByIsdIn(List<Integer> productIds);

    public List<Orders> findByUser(User user);

    String currentUserName();

    String shopPage(String searchKey, Model model);

    String productDetails(int id, Model model);

    List<User> findAll(Sort sort);

    User currentUser();

    List<Banner> allBanners();

}
