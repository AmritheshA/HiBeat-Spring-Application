package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.Repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Slf4j
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final UserServices userServices;
    private final CouponService couponService;
    private final ProductService productService;

    @Autowired
    public CartServiceImp(CartRepository cartRepository, UserServices userServices, CouponService couponService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.userServices = userServices;
        this.couponService = couponService;
        this.productService = productService;
    }

    @Override
    public Cart findByUserId(Integer id) {
        return cartRepository.findByUserId(id);
    }

    @Override
    public String cart(Model model) {
        try {

            User user = userServices.findByName(userServices.currentUserName());
            Cart cart = findByUserId(user.getId());
            Coupons coupons = couponService.findByCouponCode(cart.getUsedCoupon());

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

        } catch (Exception e) {
            return "Exception/cartNotFound";
        }
        return "User/cart";

    }

    @Override
    public ResponseEntity<String> addToCart(int productId) {

        try {
            String userName = userServices.currentUserName();
            User user = userServices.findByName(userName);
            Products product = productService.findAllById(productId);

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
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            return ResponseEntity.ok().body("failed");
        }
    }

    @Override
    public ResponseEntity<String> quantityManager(int count, int productId) {

        try {

            double totalCartAmount = 0.0;
            User user = userServices.findByName(userServices.currentUserName());
            Cart cart = cartRepository.findByUserId(user.getId());

            List<CartProduct> cartProducts = cart.getCartProducts();

            for (CartProduct product : cartProducts) {

                if (product.getProduct().getId() == productId) {
                    int currentQuantity = product.getQuantity();

                    if (count == 1 && currentQuantity < product.getProduct().getStock()) {

                        product.setQuantity(currentQuantity + 1);
                    } else if (count == -1 && currentQuantity > 1) {

                        product.setQuantity(currentQuantity - 1);
                    }

                    int quantity = product.getQuantity();
                    double price = product.getProduct().getPrice();
                    double productTotal = quantity * price;
                    totalCartAmount += productTotal;

                    cart.setTotalCartAmount(totalCartAmount);
                    cartRepository.save(cart);


                    String responce = String.valueOf(product.getProduct().getStock());

                    return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(responce);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("error");
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("product not found");
    }

    @Override
    public ResponseEntity<String> removeProductFromCart(int productId) {
        try {

            String userName = userServices.currentUserName();
            Integer userId = userServices.findByName(userName).getId();
            Cart cart = cartRepository.findByUserId(userId);
            Products products = productService.findById(productId).get();

            cart.getCartProducts().removeIf(cartProduct -> cartProduct.getProduct().getId() == productId);

            cart.setTotalCartAmount(cart.getTotalCartAmount() - products.getPrice());

            cartRepository.save(cart);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {

            log.info("removeProductFromCart"+e.getMessage());
            return ResponseEntity.ok().body("failed");
        }
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }


}