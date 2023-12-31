package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Admin;
import com.Hibeat.Hibeat.Model.Admin.Categories;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Model.Admin.Products;

import java.util.List;
import java.util.Optional;


public interface Services {
    Admin save_admin(Admin userInfo);

    Products save_product(Products productDetails);

    Admin findAllByAdminName(String adminName);

    Products findAllById(Integer productId);

    List<Products> findByCategories(int id);

    Optional<Categories> findById(int categoryId);

    List<Categories> findAll();

    List<Products> findByNameContaining(String keyword);

    Orders findByOrderId(String orderId);

}
