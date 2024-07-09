package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Brands;
import com.Hibeat.Hibeat.Model.Admin.Categories;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.BrandRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class AdminProductServiceImp implements AdminProductService {


    private final ProductRepository productRepository;
    private final AdminCategoryService adminCategoryService;
    private final ModelMapperConverter modelMapperConverter;
    private final BrandRepository brandRepository;
    private final Cloudinary cloudinary;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);


    @Autowired
    public AdminProductServiceImp(ProductRepository productRepository, AdminCategoryService adminCategoryService, ModelMapperConverter modelMapperConverter, BrandRepository brandRepository, Cloudinary cloudinary) {
        this.productRepository = productRepository;
        this.adminCategoryService = adminCategoryService;
        this.modelMapperConverter = modelMapperConverter;
        this.brandRepository = brandRepository;
        this.cloudinary = cloudinary;
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
            // Convert DTO to entity
            Products productInfo = modelMapperConverter.DTOToProduct(productDetails);

            MultipartFile[] files = productDetails.getImage();
            if (files == null || files.length == 0) {
                throw new IllegalArgumentException("No files uploaded");
            }

            String[] images = new String[files.length]; // Initialize array with the correct size

            // This help us to compute uploading for file simultaneously
            List<CompletableFuture<Void>> futures = IntStream.range(0, files.length)
                    .mapToObj(i -> CompletableFuture.runAsync(() -> {
                        try {
                            if (files[i] != null) {
                                String url = uploadImage(files[i]);
                                images[i] = url;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, executorService))
                    .toList();

            // Wait for all uploads to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            productInfo.setImages_path(images);

            // Save the product information to the database
            save(productInfo);

            return "redirect:/admin/add-product";
        } catch (IllegalArgumentException e) {
            log.error("No files uploaded: " + e.getMessage());
            return "Exception/404";
        } catch (Exception e) {
            log.error("addProducts " + e.getMessage());
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
                List<Brands> brands = brandRepository.findAll((Sort.by(Sort.Direction.ASC, "id")));

                if (!(category.isEmpty())) {
                    model.addAttribute("categories", category);
                    model.addAttribute("brands", brands);

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

            log.info("Category" + productDetails.getCategories());

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
            products.setCategories(productDetails.getCategories());
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

    // Helper methods
    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        return uploadResult.get("url").toString();
    }

}
