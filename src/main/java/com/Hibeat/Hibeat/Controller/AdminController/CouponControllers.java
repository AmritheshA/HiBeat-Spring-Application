package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.Coupons;
import com.Hibeat.Hibeat.Repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/admin")
@Slf4j
public class CouponControllers {

    CouponRepository couponRepository;

    public CouponControllers(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @GetMapping("/coupon")
    public String coupon(Model model) {

        List<Coupons> couponsList = couponRepository.findAll();

        model.addAttribute("couponsList", couponsList);

        return "Admin/Coupons";
    }

    @GetMapping("/coupon/add-coupon")
    public String addCoupon() {

        return "Admin/addCoupons";
    }

    @PostMapping("/coupon/add-coupon")
    public String add_coupon(@ModelAttribute Coupons couponDetails) {
        Coupons coupons = new Coupons();
        Set<Integer> userIds = new HashSet<>();

        coupons.setCouponName(couponDetails.getCouponName());
        coupons.setCouponCode(couponDetails.getCouponCode());
        coupons.setExpireTime(couponDetails.getExpireTime());
        coupons.setDiscountAmount(couponDetails.getDiscountAmount());
        coupons.setMinimumAmount(couponDetails.getMinimumAmount());
        coupons.setSingleOrMultiple(couponDetails.getSingleOrMultiple());
        coupons.setUserid(userIds);
        coupons.setStatus("ACTIVE");

        couponRepository.save(coupons);

        scheduleCouponExpiration(coupons);

        return "Admin/addCoupons";
    }

    @GetMapping("/coupon/edit-coupons/{id}")
    public String editCoupons(@PathVariable("id") int couponId, Model model) {

        try {

            Optional<Coupons> coupons = couponRepository.findById(couponId);

            if (coupons.isPresent()) {
                Coupons coupon = coupons.get();
                model.addAttribute("coupon", coupon);
            } else {
                log.warn("Coupon Is Null Can`t Fetch data from database");
                return "redirect:/error-page";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/error-page";
        }

        return "Admin/editCoupon";
    }

    @PostMapping("/coupon/edit-coupon")
    public String edit_coupon(@ModelAttribute Coupons couponDetails, @RequestParam("id") int couponId) {
        try {

            Optional<Coupons> coupons = couponRepository.findById(couponId);

            if (coupons.isPresent()) {
                Coupons coupon = coupons.get();

                coupon.setCouponName(couponDetails.getCouponName());
                coupon.setCouponCode(couponDetails.getCouponCode());
                coupon.setExpireTime(couponDetails.getExpireTime());
                coupon.setDiscountAmount(couponDetails.getDiscountAmount());
                coupon.setMinimumAmount(couponDetails.getMinimumAmount());
                coupon.setSingleOrMultiple(couponDetails.getSingleOrMultiple());
                coupon.setStatus("ACTIVE");

                couponRepository.save(coupon);
                scheduleCouponExpiration(coupon);

            } else {
                log.warn("Coupon Is Null Can`t Fetch data from database");
                return "redirect:/error-page";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/error-page";
        }

        return "redirect:/admin/coupon";
    }

    @GetMapping("/coupon/remove-coupon/{id}")
    public String removeCoupon(@PathVariable("id") int couponId){

        couponRepository.deleteById(couponId);

        return "redirect:/admin/coupon";
    }


    private void scheduleCouponExpiration(Coupons coupon) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = coupon.getExpireTime();
        long daysDifference = ChronoUnit.DAYS.between(currentDate, expirationDate);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> {
            // Update the status to EXPIRED
            coupon.setStatus("EXPIRED");
            couponRepository.save(coupon);
        }, daysDifference, TimeUnit.DAYS);
    }


}
