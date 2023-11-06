package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Servicess.User_Service.CouponService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@Slf4j
public class CouponController {
    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/cart/coupon-validate/{couponCode}")
    public ResponseEntity<Double[]>  couponValidation(@PathVariable("couponCode") String couponCode) {
       return couponService.couponValidation(couponCode);
    }

    @GetMapping("/cart/update-coupon/{couponCode}")
    public ResponseEntity<Double[]> updateCoupon(@PathVariable("couponCode") String couponCode) {
       return couponService.updateCoupon(couponCode);
    }

    @GetMapping("/cart/removeCoupon")
    @Transactional
    public ResponseEntity<String> removeExistingCoupon() {
       return  couponService.removeExistingCoupon();
    }
}
