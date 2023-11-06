package com.Hibeat.Hibeat.Controller.userController;


import com.Hibeat.Hibeat.Servicess.User_Service.WishlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequestMapping("/user")
@Controller
public class WishlistController {


    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/add-to-wishlist")
    public ResponseEntity<String> addToWishlist(@RequestParam("id") int productId) {
        return wishlistService.addToWishlist(productId);
    }

    @GetMapping("/wishlist")
    public String wishlist(Model model) {
       return wishlistService.wishList(model);
    }

    @GetMapping("/removeFromWishlist")
    public ResponseEntity<String> removeFromWishlist(@RequestParam("id") int productId) {
        return wishlistService.removeFromWishlist(productId);
    }
}
