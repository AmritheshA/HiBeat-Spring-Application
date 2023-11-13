package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.Admin.Categories;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.CategoryRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import com.Hibeat.Hibeat.Repository.User.UserRepository;
import com.Hibeat.Hibeat.Servicess.Admin_Service.AdminProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin")
public class ProductController {

    ProductRepository productRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    ModelMapperConverter modelMapperConverter;
    
    private final AdminProductService adminProductService;

    @Autowired
    public ProductController(ProductRepository productRepository,
                             ModelMapperConverter modelMapperConverter,
                             UserRepository userRepository,
                             CategoryRepository categoryRepository, AdminProductService adminProductService) {
        this.productRepository = productRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.adminProductService = adminProductService;
    }

    @GetMapping("/products")
    public String searchProduct(Model model){
        return adminProductService.products(model);
    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {

        List<Categories> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "Admin/addProduct";
    }


    @PostMapping("/add-product")
    public String add_product(@ModelAttribute Product_DTO productDetails) {
        return adminProductService.addProducts(productDetails);
    }

    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable("id") int product_id, Model model) {
        return adminProductService.editProduct(product_id,model);
    }

    @PostMapping("/edit-product/{id}")
    public String edit_product(@RequestParam(value = "image1",required = false) MultipartFile image1,@RequestParam(value = "image2",required = false) MultipartFile image2,@RequestParam(value = "image3",required = false) MultipartFile image3,@ModelAttribute Product_DTO productDetails,@PathVariable("id") int product_id){
       return adminProductService.editProduct(image1,image2,image3,productDetails,product_id);
    }

    @GetMapping("/disable-product/{id}")
    public String disableProduct(@PathVariable("id") int product_id) {
        return adminProductService.disableProduct(product_id);
    }

}
