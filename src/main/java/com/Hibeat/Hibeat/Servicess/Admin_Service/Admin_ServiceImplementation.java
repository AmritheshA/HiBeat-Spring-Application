package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Admin;
import com.Hibeat.Hibeat.Model.Admin.Categories;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.AdminRepository;
import com.Hibeat.Hibeat.Repository.Admin.CategoryRepository;
import com.Hibeat.Hibeat.Repository.User.OrderRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Admin_ServiceImplementation implements Services{


    AdminRepository adminRepository;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    OrderRepository orderRepository;
    ModelMapperConverter modelMapperConverter;


    @Autowired
    public Admin_ServiceImplementation(AdminRepository adminRepository,
                                       ProductRepository productRepository,
                                       ModelMapperConverter modelMapperConverter,
                                       CategoryRepository categoryRepository,
                                       OrderRepository orderRepository) {
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Admin save_admin(Admin userInfo) {
        return adminRepository.save(userInfo);
    }
    @Override
    public Products save_product(Products productDetails) {


        return productRepository.save(productDetails);
    }
    @Override
    public Admin findAllByAdminName(String adminName) {
        return adminRepository.findByAdminName(adminName);
    }
    @Override
    public Products findAllById(Integer productId) {
        return productRepository.findAllById(productId);
    }

    @Override
    public List<Products> findByCategories(int id) {
        return productRepository.findByCategories(id);
    }

    @Override
    public Optional<Categories> findById(int categoryId) {
        return categoryRepository.findById(categoryId);
    }
    @Override
    public List<Categories> findAll() {
        return categoryRepository.findAll();
    }
    @Override
    public List<Products> findByNameContaining(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }
    @Override
    public Orders findByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

}
