package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Products save(Products products) {
        return productRepository.save(products);
    }

    public List<Products> findAllProduct(){

        return productRepository.findAll();
    }

    public List<Products> searchProductByName(String searchKey) {

        return productRepository.findProductsByNameContaining(searchKey);
    }

    @Override
    public List<Products> searchProductByCategory(Integer value) {
        return productRepository.findByCategories(value);
    }

//    @Override
//    public List<Products> searchProductByBrand(String value) {
//        return productRepository.findByBrandContaining(value);
//    }

    public Products findAllById(int id) {
        return productRepository.findAllById(id);
    }

    public List<Products> findByCategories(int categoryId) {

        return productRepository.findByCategories(categoryId);
    }

    @Override
    public Optional<Products> findById(int productId) {
        return productRepository.findById(productId);
    }
}
