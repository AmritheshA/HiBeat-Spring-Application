package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Cart;
import com.Hibeat.Hibeat.Model.Coupons;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Repository.CartRepository;
import com.Hibeat.Hibeat.Repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class CouponServiceImp implements CouponService {

    private final UserServices userServices;
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CouponServiceImp(UserServices userServices,CouponRepository couponRepository, CartRepository cartRepository) {
        this.userServices = userServices;
        this.couponRepository = couponRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Coupons findByCouponCode(String usedCoupon) {
        return couponRepository.findByCouponCode(usedCoupon);
    }

    @Override
    public ResponseEntity<Double[]> couponValidation(String couponCode) {

        try {
            User user = userServices.findByName(userServices.currentUserName());
            Cart cart = cartRepository.findByUserId(user.getId());
            Coupons coupons = findByCouponCode(couponCode);

            double cartTotalAmount = cart.getTotalCartAmount();
            double couponMinAmount = coupons.getMinimumAmount();
            double couponDiscountAmount = coupons.getDiscountAmount();

            if (cartTotalAmount < couponMinAmount) {
                return ResponseEntity.ok(new Double[]{-1.0}); // Coupon is in-valid
            }

            if (cart.getUsedCoupon() != null && !cart.getUsedCoupon().isEmpty()) {
                if (cart.getUsedCoupon().equals(couponCode)) {
                    return ResponseEntity.ok(new Double[]{couponDiscountAmount, 0.0}); // Coupon already used by the user
                } else {
                    // Update the existing coupon
                    return ResponseEntity.ok(new Double[]{0.0});
                }
            }

            if (isValidCoupon(coupons, cart)) {
                Set<Integer> ids = coupons.getUserid();
                ids.add(user.getId());
                cart.setUsedCoupon(couponCode);
                coupons.setUserid(ids);
                couponRepository.save(coupons);
                cartRepository.save(cart);
                return ResponseEntity.ok(new Double[]{1.0, couponDiscountAmount});
            }

            return ResponseEntity.ok(new Double[]{-1.0}); // Coupon is in-valid

        } catch (Exception e) {
            return ResponseEntity.ok(new Double[]{-1.0}); // Error occurred
        }
    }

    @Override
    public ResponseEntity<Double[]> updateCoupon(String couponCode) {
        try {

            User user = userServices.findByName(userServices.currentUserName());
            Cart cart = cartRepository.findByUserId(user.getId());
            Coupons coupons = findByCouponCode(couponCode);

            if (isValidCoupon(coupons, cart)) {
                double couponDiscountAmount = coupons.getDiscountAmount();
                cart.setUsedCoupon(couponCode);
                couponRepository.save(coupons);
                return ResponseEntity.ok(new Double[]{1.0, couponDiscountAmount});
            }

            return ResponseEntity.ok(new Double[]{-1.0});
        } catch (Exception e) {
            log.info("updateCoupon"+e.getMessage());
            return ResponseEntity.ok(new Double[]{-1.0});
        }
    }

    private boolean isValidCoupon(Coupons coupons, Cart cart) {
        double cartTotalAmount = cart.getTotalCartAmount();
        double couponMinAmount = coupons.getMinimumAmount();

        boolean isActive = coupons.getStatus().equals("ACTIVE");
        boolean isMultiple = coupons.getSingleOrMultiple().equals("multiple");
        boolean isSingleEmpty = coupons.getSingleOrMultiple().equals("single") && coupons.getUserid().isEmpty();

        return isActive && (isMultiple || isSingleEmpty) && cartTotalAmount >= couponMinAmount;
    }

    @Override
    public ResponseEntity<String> removeExistingCoupon() {

        User user = userServices.findByName(userServices.currentUserName());
        Cart cart = cartRepository.findByUserId(user.getId());

        cart.setUsedCoupon(null);
        cartRepository.save(cart);

        return ResponseEntity.ok().body("success");
    }
}
