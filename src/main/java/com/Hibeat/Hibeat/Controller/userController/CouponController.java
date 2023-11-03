package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.Cart;
import com.Hibeat.Hibeat.Model.Coupons;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Repository.CartRepository;
import com.Hibeat.Hibeat.Repository.CouponRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/user")
@Slf4j
public class CouponController {

    CouponRepository couponRepository;
    CartRepository cartRepository;
    UserRepository userRepository;

    @Autowired
    public CouponController(CouponRepository couponRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/cart/coupon-validate/{couponCode}")
    public ResponseEntity<Double[]> couponValidation(@PathVariable("couponCode") String couponCode, Principal principal) {
        try {
            User user = userRepository.findByName(principal.getName());
            Cart cart = cartRepository.findByUserId(user.getId());
            Coupons coupons = couponRepository.findByCouponCode(couponCode);

            double cartTotalAmount = cart.getTotalCartAmount();
            double couponMinAmount = coupons.getMinimumAmount();
            double couponDiscountAmount = coupons.getDiscountAmount();

//        if the user have a coupon then show you are owned a coupon now if u continue that become in-validate;
//        if the coupon is new then add it to the cart used coupon, and don't forgot to remove that when order is placed.

            log.info("Cart Total Amount"+cartTotalAmount);

            if (cartTotalAmount >= couponMinAmount) {

                if ((cart.getUsedCoupon() == null) || (cart.getUsedCoupon().isEmpty())) {

                    if (coupons.getStatus().equals("ACTIVE") && coupons.getSingleOrMultiple().equals("multiple") ||
                            coupons.getSingleOrMultiple().equals("single") && coupons.getUserid().isEmpty()) {


                        Set<Integer> ids = coupons.getUserid();
                        ids.add(user.getId());
                        cart.setUsedCoupon(couponCode);
                        coupons.setUserid(ids);
                        couponRepository.save(coupons);
                        cartRepository.save(cart);

                    } else {
//                the coupon is in-valid
                        return ResponseEntity.ok().body(new Double[]{-1.0});
                    }

                } else {
//            coupon is already used by the user
                    if (cart.getUsedCoupon().equals(couponCode)) {
                        log.info("couponDiscount" + couponDiscountAmount);
                        return ResponseEntity.ok().body(new Double[]{couponDiscountAmount, 0.0});
                    } else {
//                        update the existing coupon
                        return ResponseEntity.ok().body(new Double[]{0.0});
                    }
                }
            } else {
                return ResponseEntity.ok().body(new Double[]{-1.0});
            }
            return ResponseEntity.ok().body(new Double[]{1.0, couponDiscountAmount});
        } catch (Exception e) {
            log.info("Error" + e.getMessage());
            return ResponseEntity.ok().body(new Double[]{-1.0});
        }

    }

    @GetMapping("/cart/update-coupon/{couponCode}")
    public ResponseEntity<Double[]> updateCoupon(@PathVariable("couponCode") String couponCode, Principal principal) {

        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());
        Coupons coupons = couponRepository.findByCouponCode(couponCode);

        double cartTotalAmount = cart.getTotalCartAmount();
        double couponMinAmount = coupons.getMinimumAmount();
        double couponDiscountAmount = coupons.getDiscountAmount();

        if (coupons.getStatus().equals("ACTIVE") && coupons.getSingleOrMultiple().equals("multiple") ||
                coupons.getSingleOrMultiple().equals("single") && coupons.getUserid().isEmpty()) {

            if (cartTotalAmount >= couponMinAmount) {
                Set<Integer> ids = coupons.getUserid();
                ids.add(user.getId());
                cart.setUsedCoupon(couponCode);
                coupons.setUserid(ids);
                couponRepository.save(coupons);
            }
        } else {
//                the coupon is in-valid
            return ResponseEntity.ok().body(new Double[]{-1.0});
        }

        return ResponseEntity.ok().body(new Double[]{1.0, couponDiscountAmount});
    }

    @GetMapping("/cart/removeCoupon")
    @Transactional
    public ResponseEntity<String> removeExistingCoupon(Principal principal) {

        log.info("Principle Name "+principal.getName());

        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());

        cart.setUsedCoupon(null);
        log.info("usedCoupon is setted to null");
        cartRepository.save(cart);

        log.info("cart is saved..");

        return ResponseEntity.ok().body("success");
    }
}
