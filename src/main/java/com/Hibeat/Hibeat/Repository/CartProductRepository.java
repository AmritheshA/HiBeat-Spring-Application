package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends   JpaRepository<CartProduct, Integer> {

}
