package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Products;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Products save(Products products);
    List<Products> findAllProduct();

    List<Products> searchProductByName(String searchKey);

    Products findAllById(int id);

    List<Products> findByCategories(int categoryId);

    Optional<Products> findById(int productId);


}
