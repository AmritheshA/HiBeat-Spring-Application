package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Servicess.User_Service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/cart")
@Slf4j
public class CartController {

    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("")
    public String cart(Model model) {
       return cartService.cart(model);
    }

    @GetMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestParam("id") int productId) {
       return cartService.addToCart(productId);
    }

    @GetMapping("/quantity-counter")
    public ResponseEntity<String> sample(@RequestParam("counter") int counter,@RequestParam("id") int productId) {
       return cartService.quantityManager(counter,productId);
    }

    @GetMapping("/remove-product/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable("productId") int productId) {
      return cartService.removeProductFromCart(productId);
    }

}