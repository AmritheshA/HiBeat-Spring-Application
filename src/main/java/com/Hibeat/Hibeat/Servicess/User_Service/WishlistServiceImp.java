package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Model.Wishlist;
import com.Hibeat.Hibeat.Model.WishlistItem;
import com.Hibeat.Hibeat.Repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WishlistServiceImp implements WishlistService {
    private final UserServices userServices;
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    @Autowired
    public WishlistServiceImp(UserServices userServices, WishlistRepository wishlistRepository, ProductService productService) {
        this.userServices = userServices;
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    @Override
    public ResponseEntity<String> addToWishlist(int productId) {
        try {

            User user = userServices.findByName(userServices.currentUserName());
            Wishlist wishlist = wishlistRepository.findByUser(user);
            Optional<Products> products = productService.findById(productId);

            if (wishlist.getWishlistItems().stream().anyMatch(wishlistItem -> wishlistItem.getProduct().equals(products.get()))) {
                return ResponseEntity.ok().body("sameProduct");
            }

            if (products.isPresent()) {
                WishlistItem wishlistItem = new WishlistItem();

                wishlistItem.setProduct(products.get());
                wishlistItem.setWishlist(wishlist);

                List<WishlistItem> wishlistItems = wishlist.getWishlistItems();
                wishlistItems.add(wishlistItem);
                wishlist.setWishlistItems(wishlistItems);

                wishlistRepository.save(wishlist);

                return ResponseEntity.ok().body("success");
            } else {
                // Product not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.info("addToWishlist" + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public String wishList(Model model) {
        try {
            User user = userServices.findByName(userServices.currentUserName());

            Wishlist wishlist = wishlistRepository.findByUser(user);

            if (wishlist == null) {
                wishlist = new Wishlist();
                wishlist.setUser(user);
                wishlistRepository.save(wishlist);
            }

            model.addAttribute("wishlistItems", wishlist.getWishlistItems());


            return "User/wishlist";
        } catch (Exception e) {
            log.info("wishlist" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public ResponseEntity<String> removeFromWishlist(int productId) {
        try {
            String username = userServices.currentUserName();

            User user = userServices.findByName(username);
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

            wishlist.getWishlistItems().remove(wishlistItem);
            wishlistRepository.save(wishlist);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.error("removeFromWishlist" + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
