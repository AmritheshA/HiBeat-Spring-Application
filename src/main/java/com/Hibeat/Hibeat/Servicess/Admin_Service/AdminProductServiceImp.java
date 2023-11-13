package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Categories;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class AdminProductServiceImp implements AdminProductService {


    private final ProductRepository productRepository;
    private final AdminCategoryService adminCategoryService;
    private final ModelMapperConverter modelMapperConverter;

    @Autowired
    public AdminProductServiceImp(ProductRepository productRepository, AdminCategoryService adminCategoryService, ModelMapperConverter modelMapperConverter) {
        this.productRepository = productRepository;
        this.adminCategoryService = adminCategoryService;
        this.modelMapperConverter = modelMapperConverter;
    }

    @Override
    public Products save(Products products) {
        return productRepository.save(products);
    }

    private List<Products> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public String products(Model model) {
        try {
            List<Products> products = findAllProduct();

            products.sort(Comparator.comparing(Products::getId));

            if (!(products.isEmpty())) {
                model.addAttribute("products", products);
            } else {
                log.info("There is no product in the database...");
            }
            return "Admin/products";
        } catch (Exception e) {
            log.info("products " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String addProducts(Product_DTO productDetails) {
        try {
            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

            Products productInfo = modelMapperConverter.DTOToProduct(productDetails);

            MultipartFile[] files = productDetails.getImage();
            String[] images = productInfo.getImages_path();

            for (int i = 0; i < files.length; i++) {
                images[i] = files[i].getOriginalFilename();
                files[i].transferTo(new File(file + files[i].getOriginalFilename()));
            }
            productInfo.setImages_path(images);


            save(productInfo);

            return "redirect:/admin/add-product";
        } catch (Exception e) {
            log.info("addProducts " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String editProduct(int productId, Model model) {
        Products products = productRepository.findAllById(productId);
        try {
            if (products != null) {
                model.addAttribute("products", products);

                List<Categories> category = adminCategoryService.findAll((Sort.by(Sort.Direction.ASC, "id")));
                if (!(category.isEmpty())) {
                    model.addAttribute("categories", category);

                    model.addAttribute("images", products.getImages_path());
                }

                if (products.getStatus().equals("IN-ACTIVE")) {
                    return "redirect:/admin/products";
                }
            }
            return "Admin/editProduct";
        } catch (Exception e) {

            log.info("editProduct " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String editProduct(MultipartFile image1, MultipartFile image2, MultipartFile image3, Product_DTO productDetails, int productId) {
        try {
            Products products = productRepository.findAllById(productId);

            log.info("Category"+productDetails.getCategory());

            String[] images = products.getImages_path();
            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

            if (image1 != null) {
                images[0] = image1.getOriginalFilename();
                image1.transferTo(new File(file + image1.getOriginalFilename()));
            }
            if (image2 != null) {
                images[1] = image2.getOriginalFilename();
                image2.transferTo(new File(file + image2.getOriginalFilename()));
            }
            if (image2 != null) {
                images[2] = image3.getOriginalFilename();
                image3.transferTo(new File(file + image3.getOriginalFilename()));
            }

            products.setImages_path(images);
            products.setName(productDetails.getProductName());
            products.setPrice(Double.parseDouble(productDetails.getPrice()));
            products.setStock(productDetails.getStock());
            products.setCategories(productDetails.getCategory());
            products.setDescription(productDetails.getDescription());
            productRepository.save(products);

            return "redirect:/admin/products";
        } catch (Exception e) {
            log.info("editProduct Post " + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String disableProduct(int productId) {
        try {
            Products products = productRepository.findAllById(productId);

            if (products != null && products.getStatus().equals("ACTIVE")) {
                products.setStatus("IN-ACTIVE");
                productRepository.save(products);
                return "redirect:/admin/products";
            } else if (products != null && products.getStatus().equals("IN-ACTIVE")) {
                products.setStatus("ACTIVE");
                productRepository.save(products);
                return "redirect:/admin/products";
            }

            return "redirect:/admin/products";
        } catch (Exception e) {
            log.info("disableProduct " + e.getMessage());
            return "Exception/404";
        }
    }


}
