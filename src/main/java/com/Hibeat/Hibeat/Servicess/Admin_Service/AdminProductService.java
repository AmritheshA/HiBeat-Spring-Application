package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface AdminProductService {

    Products save(Products products);
    String products(Model model);

    String addProducts(Product_DTO productDetails);

    String editProduct(int productId, Model model);

    String editProduct(MultipartFile image1, MultipartFile image2, MultipartFile image3, Product_DTO productDetails, int productId);

    String disableProduct(int productId);

}
