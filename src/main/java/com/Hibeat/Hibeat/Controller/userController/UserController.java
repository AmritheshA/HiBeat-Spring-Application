package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public UserController(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, CartProductRepository cartProductRepository, OrderRepository orderRepository,PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/shop")
    public String shop(Model model) {

        System.out.printf("Not null ");

        List<Products> products = productRepository.findAll();

        System.out.printf("empty");

        model.addAttribute("products", products);

        return "User/shop";
    }

    @GetMapping("/add-to-cart")
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
    public String removeProductFromCart(@PathVariable("productId") int productId,
                                        Principal principal) {

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

    @GetMapping("/checkout")
    public String checkOut(Principal principal,
                           Model model) {

        String userName = principal.getName();
        User user = userRepository.findByName(userName);

        Cart cart = cartRepository.findByUserId(user.getId());

        List<CartProduct> cartProducts = cart.getCartProducts();

        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("addresses", user.getAddresses());
        model.addAttribute("totalAmount", cart.getTotalCartAmount());


        return "User/checkout";
    }

    @GetMapping("/new-address")
    public String newAddress() {
        return "User/NewAddress";
    }

    @GetMapping("/update-address")
    public String updateAddress(@RequestParam("index") int index,
                                Principal principal,
                                Model model) {

        User user = userRepository.findByName(principal.getName());

        model.addAttribute("address", user.getAddresses().get(index));
        model.addAttribute("address_index", index);


        return "User/updateAddress";
    }

    @PostMapping("/new-address")
    public String newAddress(@ModelAttribute Address addressDetails, Principal principal) {
        User user = userRepository.findByName(principal.getName());

        // Set the user's mobile
        user.setMobile(addressDetails.getMobile());

        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }

        user.getAddresses().add(addressDetails);

        userRepository.save(user);

        return "redirect:/user/checkout";
    }


    @PostMapping("/update-address")
    public String update_address(@ModelAttribute Address addressDetails,
                                 @RequestParam("index") int addressIndex,
                                 Principal principal) {


        log.info("addressIndex " + addressIndex);

        User user = userRepository.findByName(principal.getName());

        user.setMobile(addressDetails.getMobile());

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
                                @RequestParam("mobile") String mobile,
                                Principal principal){

        User user = userRepository.findByName(principal.getName());

        user.setName(username);
        user.setEmail(email);
        user.setMobile(mobile);

        userRepository.save(user);



        return "redirect:/user/profile";
    }

    @GetMapping("/resetUserPassword")
    public String resetUserPassword(@RequestParam("oldPassword") String oldPassword,
                                    @RequestParam("newPassword") String newPassword,
                                    Principal principal) {
        User user = userRepository.findByName(principal.getName());

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {

                user.setPassword(passwordEncoder.encode(newPassword));
                // Save the updated password
                userRepository.save(user);
                return "redirect:/user/profile"; // Redirect to a success page
        } else {
            return "redirect:/resetUserPassword?error=oldPasswordMismatch"; // Redirect with an error parameter
        }
    }



    @GetMapping("/wallet")
    public String wallet() {
        return "User/wallet";
    }

    @GetMapping("/wishlist")
    public String whislist() {

        return "User/wishlist";
    }


    @GetMapping("/my-address")
    public String myAddress(Principal principal,Model model) {

        User user = userRepository.findByName(principal.getName());
        
        List<Address> addresses = user.getAddresses();
        
        model.addAttribute("addresses",addresses);

        return "User/address";
    }
    @GetMapping("/remove-addressess")
    public String removeAddressessamps(@RequestParam("index") int index, Principal principal) {

        User user = userRepository.findByName(principal.getName());

        user.getAddresses().remove(index);
        userRepository.save(user);

        return "redirect:/user/my-address";

    }

    @GetMapping("/sample")
    public ResponseEntity<String> sample() {
        return ResponseEntity.ok().body("Success");
    }


}
