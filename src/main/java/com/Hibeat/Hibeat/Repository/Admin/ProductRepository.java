package com.Hibeat.Hibeat.Repository.Admin;

import com.Hibeat.Hibeat.Model.Admin.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer> {


    Products findAllById(Integer productId);

    List<Products> findByCategories(int id);

    List<Products> findByNameContaining(String keyword);

    List<Products> findAllByIdIn(List<Integer> productIds);

    @Query("SELECT p FROM Products p WHERE p.name LIKE %:keyword%")
    List<Products> findProductsByNameContaining(@Param("keyword") String keyword);

}

