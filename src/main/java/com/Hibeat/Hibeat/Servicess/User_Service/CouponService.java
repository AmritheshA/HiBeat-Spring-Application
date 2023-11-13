package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Coupons;
import org.springframework.http.ResponseEntity;

public interface CouponService {

    Coupons findByCouponCode(String usedCoupon);

    ResponseEntity<Double[]> couponValidation(String couponCode);

    ResponseEntity<Double[]> updateCoupon(String couponCode);

    ResponseEntity<String> removeExistingCoupon();

}
