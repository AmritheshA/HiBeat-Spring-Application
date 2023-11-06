package com.Hibeat.Hibeat.Servicess.User_Service;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface WishlistService {

    ResponseEntity<String> addToWishlist(int productId);

    String wishList(Model model);

    ResponseEntity<String> removeFromWishlist(int productId);

}
