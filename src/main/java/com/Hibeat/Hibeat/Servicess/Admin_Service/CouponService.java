package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Coupons;
import org.springframework.ui.Model;

import java.util.List;

public interface CouponService {

    Coupons save(Coupons coupons);
    List<Coupons> findAll();

    String coupons(Model model);

    String addCoupon(Coupons couponDetails);

    String editCoupon(int couponId, Model model);

    String editCoupon(Coupons couponDetails, int couponId);

    String removeCoupon(int couponId);

}
