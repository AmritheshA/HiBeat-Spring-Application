package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Configuration.CustomUserDetailService;
import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    ProductRepository productRepository;
    UserRepository userRepository;
    CartRepository cartRepository;
    OrderRepository orderRepository;
    PasswordEncoder passwordEncoder;
    CartProductRepository cartProductRepository;
    CustomUserDetailService customUserDetailService;
    CouponRepository couponRepository;

    @Autowired
    public UserController(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, CartProductRepository cartProductRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder, CustomUserDetailService customUserDetailService, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailService = customUserDetailService;
        this.couponRepository = couponRepository;

    }

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "searchKey",required = false) String searchKey, Model model) {
        if (searchKey == null) {
            List<Products> products = productRepository.findAll();
            model.addAttribute("products", products);
        } else {
            List<Products> productsList = searchProductByName(searchKey);

            if (!(productsList.isEmpty())) {
                model.addAttribute("products", productsList);
            }
        }
        return "User/shop";
    }

    @ModelAttribute("userName")
    public String getUserName(Principal principal) {
        if (principal != null) {
            return principal.getName();
        }
        return "Login";
    }

    private List<Products> searchProductByName(String searchKey) {

        return productRepository.findProductsByNameContaining(searchKey);

    }




    @GetMapping("/quantity-counter")
    public ResponseEntity<String> sample(@RequestParam("counter") int counter, @RequestParam("id") int productId, Principal principal, Model model) {

        int targetProductId = productId;
        double totalCartAmount = 0.0;

        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());

        List<CartProduct> cartProducts = cart.getCartProducts();

        for (CartProduct product : cartProducts) {

            if (product.getProduct().getId() == targetProductId) {
                int currentQuantity = product.getQuantity();

                if (counter == 1 && currentQuantity < product.getProduct().getStock()) {

                    product.setQuantity(currentQuantity + 1);
                } else if (counter == -1 && currentQuantity > 1) {

                    product.setQuantity(currentQuantity - 1);
                }


                int quantity = product.getQuantity();
                double price = product.getProduct().getPrice();
                double productTotal = quantity * price;
                totalCartAmount += productTotal;

                log.info("cart total" + totalCartAmount);
                cart.setTotalCartAmount(totalCartAmount);
                cartRepository.save(cart);


                String responce = String.valueOf(product.getProduct().getStock());

                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(responce);
            }
        }


        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("product not found");
    }


    @GetMapping("/remove-product/{productId}")
    public String removeProductFromCart(@PathVariable("productId") int productId, Principal principal) {

        String userName = principal.getName();
        Integer userId = userRepository.findByName(userName).getId();
        Cart cart = cartRepository.findByUserId(userId);
        Products products = productRepository.findById(productId).get();

        cart.getCartProducts().removeIf(cartProduct -> cartProduct.getProduct().getId() == productId);


        cart.setTotalCartAmount(cart.getTotalCartAmount() - products.getPrice());

        cartRepository.save(cart);

        return "redirect:/user/cart";
    }

    @GetMapping("/product-details/{id}")
    public String productDetails(@PathVariable("id") int id, Model model) {

        Products product = productRepository.findAllById(id);

        int categoryId = product.getCategories();

        if (product != null) {
            model.addAttribute("product", product);

            if (categoryId != 0) {
                List<Products> products = productRepository.findByCategories(categoryId);
                List<Products> limitedProducts = products.stream().limit(3).toList();

                model.addAttribute("relatedProduct", limitedProducts);
            }
        }

        return "User/productDetails";
    }

    @GetMapping("/home")
    public String home(Model model,Principal principal) {

        return "User/home";
    }


    @GetMapping("/new-address")
    public String newAddress() {
        return "User/NewAddress";
    }


    @GetMapping("/update-address")
    public String updateAddress(@RequestParam("index") int index, Principal principal, Model model) {

        User user = userRepository.findByName(principal.getName());

        model.addAttribute("address", user.getAddresses().get(index));
        model.addAttribute("address_index", index);


        return "User/updateAddress";
    }

    @PostMapping("/new-address")
    public String newAddress(@RequestParam("name") String name,
                             @RequestParam("mobile") String mobile,
                             @RequestParam("address") String address,
                             @RequestParam("locality") String locality,
                             @RequestParam("city") String city,
                             @RequestParam("pin") String pin,
                             Principal principal) {
        User user = userRepository.findByName(principal.getName());

        // Set the user's mobile
        user.setMobile(mobile);

        Address addressDetails = new Address();
        addressDetails.setAddress(address);
        addressDetails.setName(name);
        addressDetails.setLocality(locality);
        addressDetails.setCity(city);
        addressDetails.setPin(pin);
        addressDetails.setMobile(mobile);
        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }

        user.getAddresses().add(addressDetails);

        userRepository.save(user);

        return "redirect:/user/checkout";
    }


    @PostMapping("/update-address")
    public String update_address(@RequestParam("name") String name,
                                 @RequestParam("mobile") String mobile,
                                 @RequestParam("address") String address,
                                 @RequestParam("locality") String locality,
                                 @RequestParam("city") String city,
                                 @RequestParam("pin") String pin,
                                 @RequestParam("index") int addressIndex,
                                 Principal principal) {

        User user = userRepository.findByName(principal.getName());
        Address addressDetails = new Address();

        addressDetails.setAddress(address);
        addressDetails.setName(name);
        addressDetails.setLocality(locality);
        addressDetails.setCity(city);
        addressDetails.setPin(pin);
        addressDetails.setMobile(mobile);

        user.setMobile(mobile);

        user.getAddresses().set(addressIndex, addressDetails);

        userRepository.save(user);

        return "redirect:/user/checkout";

    }

    @GetMapping("/remove-address")
    public String removeAddress(@RequestParam("index") int index, Principal principal) {

        User user = userRepository.findByName(principal.getName());

        user.getAddresses().remove(index);
        userRepository.save(user);

        return "redirect:/user/checkout";

    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {

        User user = userRepository.findByName(principal.getName());
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            log.info("User Is Null");
        }

        return "User/profile";
    }

    @GetMapping("/updateProfile")
    public String updateProfile(@RequestParam("name") String username,
                                @RequestParam("email") String email,
                                @RequestParam("mobile") String mobile, Principal principal) {

        User user = userRepository.findByName(principal.getName());


        user.setName(username);
        user.setEmail(email);
        user.setMobile(mobile);

        userRepository.save(user);

//        I use principle.getName for fetching user Details,
//        so i update the current security context principle with updated userDetails
        UserDetails updatedUserDetails = customUserDetailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, null, updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return "redirect:/user/profile";
    }

    @GetMapping("/resetUserPassword")
    public ResponseEntity<String> resetUserPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal) {
        User user = userRepository.findByName(principal.getName());


        if (passwordEncoder.matches(oldPassword, user.getPassword())) {

            user.setPassword(passwordEncoder.encode(newPassword));
            // Save the updated password
            userRepository.save(user);

        } else {
            return ResponseEntity.ok().body("success");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/my-address")
    public String myAddress(Principal principal, Model model) {

        User user = userRepository.findByName(principal.getName());

        List<Address> addresses = user.getAddresses();

        model.addAttribute("addresses", addresses);

        return "User/address";
    }

    @GetMapping("/remove-addressess")
    public String removeAddersses(@RequestParam("index") int index, Principal principal) {

        User user = userRepository.findByName(principal.getName());

        user.getAddresses().remove(index);
        userRepository.save(user);

        return "redirect:/user/my-address";

    }


    @GetMapping("/orderDetails")
    public String orderDetails(@RequestParam("orderId") String orderId, Model model, Principal principal) {


        Orders orders = orderRepository.findByOrderId(orderId);
        User user = userRepository.findByName(principal.getName());
        model.addAttribute("orders", orders);
        model.addAttribute("address", user.getAddresses().get(orders.getAddressIndex()));
        return "User/OrderDetails";
    }

    @GetMapping("/sample")
    public String sample() {

        return "sam";
    }


}
