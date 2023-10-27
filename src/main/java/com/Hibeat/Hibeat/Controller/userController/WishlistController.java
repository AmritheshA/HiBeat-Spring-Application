package com.Hibeat.Hibeat.Controller.userController;


import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Model.Wishlist;
import com.Hibeat.Hibeat.Model.WishlistItem;
import com.Hibeat.Hibeat.Repository.ProductRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import com.Hibeat.Hibeat.Repository.WishlistItemRepository;
import com.Hibeat.Hibeat.Repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/user")
@Controller
public class WishlistController {


    UserRepository userRepository;
    WishlistRepository wishlistRepository;
    ProductRepository productRepository;
    WishlistItemRepository wishlistItemRepository;

    public WishlistController(UserRepository userRepository, WishlistRepository wishlistRepository, ProductRepository productRepository, WishlistItemRepository wishlistItemRepository) {
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.wishlistItemRepository = wishlistItemRepository;
    }

    @GetMapping("/add-to-wishlist")
    public ResponseEntity<String> addToWishlist(@RequestParam("id") int productId, Principal principal) {
        User user = userRepository.findByName(principal.getName());
        Wishlist wishlist = wishlistRepository.findByUser(user);
        Optional<Products> products = productRepository.findById(productId);

        if (wishlist.getWishlistItems().stream().anyMatch(wishlistItem -> wishlistItem.getProduct().equals(products.get()))) {
             return  ResponseEntity.ok().body("sameProduct");
        }

        if (products.isPresent()) {
            WishlistItem wishlistItem = new WishlistItem();

            wishlistItem.setProduct(products.get());
            wishlistItem.setWishlist(wishlist);

            if (wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setUser(user);
                wishlistRepository.save(wishlist);
            }

            List<WishlistItem> wishlistItems = wishlist.getWishlistItems();
            wishlistItems.add(wishlistItem);
            wishlist.setWishlistItems(wishlistItems);

            wishlistRepository.save(wishlist);

            return  ResponseEntity.ok().body("success");
        } else {
            // Product not found
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/wishlist")
    public String wishlist(Principal principal, Model model) {

        User user = userRepository.findByName(principal.getName());

        Wishlist wishlist = wishlistRepository.findByUser(user);

        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUser(user);
            wishlistRepository.save(wishlist);
        }

        model.addAttribute("wishlistItems", wishlist.getWishlistItems());


        return "User/wishlist";
    }

    @GetMapping("/removeFromWishlist")
    public ResponseEntity<String> removeFromWishlist(@RequestParam("id") int productId, Principal principal) {


        // Get the currently logged-in user
        String username = principal.getName();

        User user = userRepository.findByName(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Wishlist wishlist = wishlistRepository.findByUser(user);
        if (wishlist == null) {
            return ResponseEntity.notFound().build();
        }

        WishlistItem wishlistItem = wishlist.getWishlistItems().stream()
                .filter(wishlistItems -> wishlistItems.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);

        if (wishlistItem == null) {
            return ResponseEntity.notFound().build();
        }

        log.info("WishlistItem " + wishlistItem.getProduct().getName());

        try {
            wishlist.getWishlistItems().remove(wishlistItem); // Remove the item from the list
            wishlistRepository.save(wishlist);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.error("Error while removing item from wishlist: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
