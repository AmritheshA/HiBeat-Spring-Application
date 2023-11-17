package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends   JpaRepository<CartProduct, Integer> {

    void deleteByCartId(Integer cartId);


}
