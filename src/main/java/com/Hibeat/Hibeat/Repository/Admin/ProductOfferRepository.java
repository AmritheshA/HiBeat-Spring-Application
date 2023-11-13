package com.Hibeat.Hibeat.Repository.Admin;

import com.Hibeat.Hibeat.Model.Admin.ProductOffers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOfferRepository extends JpaRepository<ProductOffers,Integer> {
}
