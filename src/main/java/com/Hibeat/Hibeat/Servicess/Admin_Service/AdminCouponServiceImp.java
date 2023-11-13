package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Coupons;
import com.Hibeat.Hibeat.Repository.Admin.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AdminCouponServiceImp implements CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public AdminCouponServiceImp(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupons save(Coupons coupons) {
        return couponRepository.save(coupons);
    }

    @Override
    public List<Coupons> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public String coupons(Model model) {
        try {
            List<Coupons> couponsList = findAll();
            model.addAttribute("couponsList", couponsList);

            return "Admin/Coupons";
        } catch (Exception e) {
            log.info("coupons" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String addCoupon(Coupons couponDetails) {

        try {

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

            save(coupons);

            scheduleCouponExpiration(coupons);

            return "Admin/addCoupons";
        }catch (Exception e){
            log.info("addCoupons1"+e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String editCoupon(int couponId, Model model) {
        try {

            Optional<Coupons> coupons = couponRepository.findById(couponId);

            if (coupons.isPresent()) {
                Coupons coupon = coupons.get();
                model.addAttribute("coupon", coupon);
            } else {
                return "Exception/404";
            }
        } catch (Exception e) {
            log.info("editCoupon"+e.getMessage());
            return "Exception/404";
        }

        return "Admin/editCoupon";
    }

    @Override
    public String editCoupon(Coupons couponDetails, int couponId) {
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
                log.info("editCoupon2 isPresent() is false");
                return "Exception/404";
            }
            return "redirect:/admin/coupon";
        } catch (Exception e) {
            log.info("editCoupon2"+e.getMessage());
            return "Exception/404";
        }


    }

    @Override
    public String removeCoupon(int couponId) {
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
