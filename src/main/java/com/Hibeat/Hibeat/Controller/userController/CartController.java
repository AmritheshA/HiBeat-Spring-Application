package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Configuration.CustomUserDetailService;
import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.Repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class CartController {

    ProductRepository productRepository;
    UserRepository userRepository;
    CartRepository cartRepository;
    OrderRepository orderRepository;
    PasswordEncoder passwordEncoder;
    CartProductRepository cartProductRepository;
    CustomUserDetailService customUserDetailService;
    CouponRepository couponRepository;

    public CartController(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder, CartProductRepository cartProductRepository, CustomUserDetailService customUserDetailService, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartProductRepository = cartProductRepository;
        this.customUserDetailService = customUserDetailService;
        this.couponRepository = couponRepository;
    }


    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {

        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());
        Coupons coupons = couponRepository.findByCouponCode(cart.getUsedCoupon());

        List<CartProduct> cartProducts = cart.getCartProducts();


        if (cartProducts != null) {

            model.addAttribute("cardProducts", cartProducts);
            model.addAttribute("cartIsEmpty", false);

            if (!(cartProducts.isEmpty()) && coupons != null && cart.getTotalCartAmount() > coupons.getDiscountAmount()) {
                model.addAttribute("couponsDiscount", coupons.getDiscountAmount());
                model.addAttribute("couponMinAmount", coupons.getMinimumAmount());
            } else {
                model.addAttribute("couponsDiscount", 0);
            }
        } else {
            model.addAttribute("cartIsEmpty", true);
        }
        model.addAttribute("currentTotal", cart.getTotalCartAmount());


        return "User/cart";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("id") int productId, Principal principal, Model model) {

        String userName = principal.getName();
        User user = userRepository.findByName(userName);
        Products product = productRepository.findAllById(productId);

        Cart userCart = cartRepository.findByUser(user);
        userCart.setUser(user);


        List<CartProduct> cartProducts = userCart.getCartProducts();

        if (cartProducts.stream().noneMatch(cp -> cp.getProduct().getId() == productId)) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProduct(product);
            cartProduct.setCart(userCart);
            cartProducts.add(cartProduct);
            cartRepository.save(userCart);
        }

        // Calculate the totalCartAmount
        double totalCartAmount = 0.0;
        for (CartProduct products : cartProducts) {
            int quantity = products.getQuantity();
            double price = products.getProduct().getPrice();
            double productTotal = quantity * price;
            totalCartAmount += productTotal;
        }

        // Set the totalCartAmount in the userCart
        userCart.setTotalCartAmount(totalCartAmount);
        cartRepository.save(userCart);

        return "redirect:/user/cart";
    }


}
