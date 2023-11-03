package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Coupons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupons, Integer> {

    Coupons findByCouponCode(String couponCode);

}
