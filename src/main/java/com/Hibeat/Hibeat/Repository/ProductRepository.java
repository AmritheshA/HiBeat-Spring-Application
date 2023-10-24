package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer> {

    Products findAllById(Integer productId);

    List<Products> findByCategories(int id);

    List<Products> findByNameContaining(String keyword);

    List<Products> findAllByIdIn(List<Integer> productIds);

}

