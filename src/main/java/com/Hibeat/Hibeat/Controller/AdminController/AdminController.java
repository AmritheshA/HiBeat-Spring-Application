package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.Categories;
import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.CategoryRepository;
import com.Hibeat.Hibeat.Repository.ProductRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import org.apache.tomcat.util.modeler.modules.ModelerSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@Controller
@RequestMapping("/admin")
public class AdminController {

    ProductRepository productRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    ModelMapperConverter modelMapperConverter;

    @Autowired
    public AdminController(ProductRepository productRepository,
                           ModelMapperConverter modelMapperConverter,
                           UserRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/products")
    public String searchProduct(Model model) {


        List<Products> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        if (!(products.isEmpty())) {
            model.addAttribute("products", products);
        }else {
            System.out.printf("False.....");
        }
        return "Admin/products";
    }

    @GetMapping("/product/search-product")
    public String product(@RequestParam("value") String value, Model model) {

        List<Products> product = productRepository.findByNameContaining(value);

        if (!(product.isEmpty())) {

            model.addAttribute("product", product);

        }
        return "Admin/searchResult";

    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {

        List<Categories> categories = categoryRepository.findAll();

        model.addAttribute("categories",categories);
        return "Admin/addProduct";
    }

    @PostMapping("/add-product")
    public String add_product(@ModelAttribute Product_DTO productDetails) throws IOException {

        String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

        String filepath = file + productDetails.getImage().getOriginalFilename();
        Products productInfo = modelMapperConverter.DTOToProduct(productDetails);

        productInfo.setImage_path(productDetails.getImage().getOriginalFilename());

        productRepository.save(productInfo);

        productDetails.getImage().transferTo(new File(filepath));

        return "redirect:/admin/add-product";
    }

    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable("id") int product_id, Model model) {

        Products products = productRepository.findAllById(product_id);

        if (products != null){
            model.addAttribute("products",products);
        }

        List<Categories> categorys = categoryRepository.findAll((Sort.by(Sort.Direction.ASC, "id")));
        if (!(categorys.isEmpty())) {
            model.addAttribute("categories", categorys);
        }

        if (products.getStatus().equals("IN-ACTIVE")) {
            return "redirect:/admin/products";
        }

        return "Admin/editProduct";
    }

    @PostMapping("/edit-product/{id}")
    public String edit_product(@ModelAttribute Product_DTO productDetails,
                               @PathVariable("id") int product_id) throws IOException {

        Products products = productRepository.findAllById(product_id);

        if (products != null && products.getStatus().contains("ACTIVE")) {
            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

            String filepath = file + productDetails.getImage().getOriginalFilename();
            products.setImage_path(productDetails.getImage().getOriginalFilename());
            products.setName(productDetails.getProductName());
            products.setPrice(productDetails.getPrice());
            products.setStock(productDetails.getStock());

            productRepository.save(products);

            productDetails.getImage().transferTo(new File(filepath));

            return "redirect:/admin/products";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/disableproduct/{id}")
    public String disableProduct(@PathVariable("id") int product_id) {

        Products products = productRepository.findAllById(product_id);

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
    }

    @GetMapping("/customers")
    public String customers(Model model) {

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        if (!(users.isEmpty())) {
            model.addAttribute("users", users);
        }

        return "Admin/customers";
    }

    @GetMapping("/blockuser/{id}")
    public String blockUser(@PathVariable("id") int product_id){
        User user = (User) userRepository.findAllById(product_id);

        if (user != null && user.getStatus().equals("UN-BLOCKED")) {
            user.setStatus("BLOCKED");
            userRepository.save(user);
            return "redirect:/admin/customers";
        } else if (user != null && user.getStatus().equals("BLOCKED")) {
            user.setStatus("UN-BLOCKED");
            userRepository.save(user);
            return "redirect:/admin/customers";
        }

        return "redirect:/admin/customers";
    }

    @GetMapping("/user/search-user")
    public String serarchUser(@RequestParam("value") String value, Model model) {

        List<User> users = userRepository.findByNameContaining(value);

        if (!(users.isEmpty())) {

            model.addAttribute("users", users);

        }
        return "Admin/userSearch";

    }

    @GetMapping("/categories")
    public String categories(Model model) {

        List<Categories> category = categoryRepository.findAll((Sort.by(Sort.Direction.ASC, "id")));
        if (!(category.isEmpty())) {
            model.addAttribute("categories", category);
        }

        return "Admin/categories";
    }

    @GetMapping("/add-categories")
    public String addCategories() {
        return "Admin/addCategories";
    }

    @PostMapping("/add-categories")
    public String add_categories(@RequestParam("newCategory") String newCategory) {

        Categories categories = new Categories();
        categories.setCategoryName(newCategory);
        categoryRepository.save(categories);

        return "redirect:/admin/categories";
    }

    @GetMapping("/edit-categories/{id}")
    public String editCategories(@PathVariable("id") int categoryId,Model model){

        Optional<Categories> categories = categoryRepository.findById(categoryId);

        if (categories.isPresent()) {
            Categories category = categories.get();

            if (category.getStatus().equals("ACTIVE")) {

                return "Admin/editCategory";
            }
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/edit-categories/{id}")
    public String edit_categories(@ModelAttribute Categories categories,
                                    @PathVariable("id") int categoryId){
        System.out.printf("cattt"+categories.getCategoryName());

        Optional<Categories> categorys = categoryRepository.findById(categoryId);
        if (categorys.isPresent()) {
            Categories category = categorys.get();

            category.setCategoryName(categories.getCategoryName());
            System.out.printf("Updated Cat"+category.getCategoryName());

            categoryRepository.save(category);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/disableCategory/{id}")
    public String disableCategory(@PathVariable("id") int id ) {

        Optional<Categories> categories = categoryRepository.findById(id);

        if (categories.isPresent()) {

            Categories category = categories.get();

            if (category.getStatus().equals("ACTIVE")) {

                category.setStatus("IN-ACTIVE");

            } else if (category.getStatus().equals("IN-ACTIVE")) {

                category.setStatus("ACTIVE");
            }
            categoryRepository.save(category);
        }

        return "redirect:/admin/categories";
    }



//    For Testing Purposse
    @GetMapping("/sample")
    public String sample() {

        return "/sam";
    }

}
