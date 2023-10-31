package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Configuration.CustomUserDetailService;
import com.Hibeat.Hibeat.Configuration.CustomUserDetails;
import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.Security;
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

    @Autowired
    public UserController(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, CartProductRepository cartProductRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder,CustomUserDetailService customUserDetailService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailService = customUserDetailService;

    }

    @GetMapping("/shop")
    public String shop(Model model) {

        System.out.printf("Not null ");

        List<Products> products = productRepository.findAll();

        System.out.printf("empty");

        model.addAttribute("products", products);

        return "User/shop";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("id") int productId, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.findByName(userName);
        Products product = productRepository.findAllById(productId);

        Cart userCart = cartRepository.findByUser(user);

        if (userCart == null) {
            userCart = new Cart();
            userCart.setUser(user);
            cartRepository.save(userCart);
        }

        List<CartProduct> cartProducts = userCart.getCartProducts();

        if (cartProducts == null) {
            cartProducts = new ArrayList<>();
        }

        if (cartProducts.stream().noneMatch(cp -> cp.getProduct().getId() == productId)) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProduct(product);
            cartProduct.setCart(userCart);
            cartProducts.add(cartProduct);
            cartRepository.save(userCart);
        }

        return "redirect:/user/cart";
    }


    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {

        double total = 0.0;


        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
        }
        List<CartProduct> cartProducts = cart.getCartProducts();


        if (cartProducts != null) {

            for (CartProduct product : cartProducts) {

                int quantity = product.getQuantity();
                double price = product.getProduct().getPrice();

                double productTotal = quantity * price;

                total += productTotal;

            }
            model.addAttribute("cardProducts", cartProducts);
            model.addAttribute("cartIsEmpty", false);

        } else {

            model.addAttribute("cartIsEmpty", true);
        }


        model.addAttribute("currentTotal", total);
        cart.setTotalCartAmount(total);

//        Total amount in the cart..
        cartRepository.save(cart);

        return "User/cart";
    }

    @GetMapping("/quantity-counter")
    public ResponseEntity<String> sample(@RequestParam("counter") int counter, @RequestParam("id") int productId, Principal principal, Model model) {

        int targetProductId = productId;

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

        cart.getCartProducts().removeIf(cartProduct -> cartProduct.getProduct().getId() == productId);

        log.info("Product is removed...");

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
    public String home() {

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
            System.out.println("sample........");
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
    public String sample(){

        return "sample";
    }


}
