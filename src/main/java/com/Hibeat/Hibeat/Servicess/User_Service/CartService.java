package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.User.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface CartService {

    Cart findByUserId(Integer id);

    String cart(Model model);

    String addToCart(int productId) throws Exception;

    ResponseEntity<String> quantityManager(int count, int productId);

    ResponseEntity<String> removeProductFromCart(int productId);

    void saveCart(Cart cart);

    ResponseEntity<String> addToCartFromShop(int productId);

}
