package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.Coupons;
import com.Hibeat.Hibeat.Servicess.Admin_Service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@Slf4j
public class CouponControllers {
    private final CouponService couponService;

    @Autowired
    public CouponControllers(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupon")
    public String coupon(Model model) {
       return couponService.coupons(model);
    }

    @GetMapping("/coupon/add-coupon")
    public String addCoupon() {
        return "Admin/addCoupons";
    }

    @PostMapping("/coupon/add-coupon")
    public String add_coupon(@ModelAttribute Coupons couponDetails) {
       return couponService.addCoupon(couponDetails);
    }

    @GetMapping("/coupon/edit-coupons/{id}")
    public String editCoupons(@PathVariable("id") int couponId, Model model) {
       return couponService.editCoupon(couponId,model);
    }

    @PostMapping("/coupon/edit-coupon")
    public String edit_coupon(@ModelAttribute Coupons couponDetails, @RequestParam("id") int couponId) {
        return couponService.editCoupon(couponDetails,couponId);
    }

    @GetMapping("/coupon/remove-coupon/{id}")
    public String removeCoupon(@PathVariable("id") int couponId){
        return couponService.removeCoupon(couponId);
    }
}
