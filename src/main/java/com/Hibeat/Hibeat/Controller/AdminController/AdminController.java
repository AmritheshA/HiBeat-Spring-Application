package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.Categories;
import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.CategoryRepository;
import com.Hibeat.Hibeat.Repository.ProductRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@Slf4j
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

        List<Products> products = productRepository.findAll();


        if (!(products.isEmpty())) {
            model.addAttribute("products", products);
        } else {
            log.info("There is no product in the database...");
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

        model.addAttribute("categories", categories);
        return "Admin/addProduct";
    }


    @PostMapping("/add-product")
    public String add_product(@ModelAttribute Product_DTO productDetails) throws IOException {

        String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

        Products productInfo = modelMapperConverter.DTOToProduct(productDetails);

        MultipartFile[] files = productDetails.getImage();
        String[] images = productInfo.getImages_path();

        for (int i = 0; i < files.length; i++) {
            images[i] = files[i].getOriginalFilename();
            files[i].transferTo(new File(file + files[i].getOriginalFilename()));
        }
        productInfo.setImages_path(images);

        productRepository.save(productInfo);


        return "redirect:/admin/add-product";
    }

    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable("id") int product_id, Model model) {

        Products products = productRepository.findAllById(product_id);

        if (products != null) {
            model.addAttribute("products", products);
        }

        List<Categories> categorys = categoryRepository.findAll((Sort.by(Sort.Direction.ASC, "id")));
        if (!(categorys.isEmpty())) {
            model.addAttribute("categories", categorys);
            assert products != null;
            model.addAttribute("images", products.getImages_path());
        }

        if (products.getStatus().equals("IN-ACTIVE")) {
            return "redirect:/admin/products";
        }

        return "Admin/editProduct";
    }

    @PostMapping("/edit-product/{id}")
    public String edit_product(@RequestParam(value = "image1",required = false) MultipartFile image1,
                               @RequestParam(value = "image2",required = false) MultipartFile image2,
                               @RequestParam(value = "image3",required = false) MultipartFile image3,
                               @ModelAttribute Product_DTO productDetails,
                               @PathVariable("id") int product_id) throws IOException {

        Products products = productRepository.findAllById(product_id);

        String[] images = products.getImages_path();
        String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";

        if(image1 != null) {
            images[0] = image1.getOriginalFilename();
            image1.transferTo(new File(file + image1.getOriginalFilename()));
        }

        if(image2 != null) {
            images[1] = image2.getOriginalFilename();
            image2.transferTo(new File(file + image2.getOriginalFilename()));
        }

        if(image2 != null) {
            images[2] = image3.getOriginalFilename();
            image3.transferTo(new File(file + image3.getOriginalFilename()));
        }

        products.setImages_path(images);
        products.setName(productDetails.getProductName());
        products.setPrice(Double.parseDouble(productDetails.getPrice()));
        products.setStock(productDetails.getStock());
        products.setDescription(productDetails.getDescription());
        log.info("Product Description" +productDetails.getDescription());

        productRepository.save(products);


        return "redirect:/admin/products";
    }

//    @PostMapping("/edit-product/{id}")
//    public String edit_product(@ModelAttribute Product_DTO productDetails,
//                               @PathVariable("id") int product_id) throws IOException {
//
//        Products products = productRepository.findAllById(product_id);
//
//        if (products != null && products.getStatus().contains("ACTIVE")) {
//            String file = "D:\\Brocamp_Task\\week_11\\Project\\Hibeat\\src\\main\\resources\\static\\uploads\\";
//
//
//            MultipartFile[] files = productDetails.getImage();
//            String[] images = products.getImages_path();
//
//            for (int i = 0; i < files.length; i++) {
//                images[i] = files[i].getOriginalFilename();
//                files[i].transferTo(new File(file + files[i].getOriginalFilename()));
//            }
//
//            products.setImages_path(images);
//            products.setName(productDetails.getProductName());
//            products.setPrice(Double.parseDouble(productDetails.getPrice()));
//            products.setStock(productDetails.getStock());
//            products.setDescription(productDetails.getProductDescription());
//
//            productRepository.save(products);
//
//
//            return "redirect:/admin/products";
//        }
//        return "redirect:/admin/products";
//    }

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
    public String blockUser(@PathVariable("id") int product_id) {
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


    @GetMapping("/edit-categories")
    public String edit_categories(@RequestParam("id") int id,
                                  @RequestParam("newName") String newCategoryName) {

        Optional<Categories> categorys = categoryRepository.findById(id);
        Categories category = categorys.get();


        if (categorys.isPresent() && category.getStatus().equals("ACTIVE")) {

            category.setCategoryName(newCategoryName);

            log.info("updated Categories" + newCategoryName);
            categoryRepository.save(category);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/disableCategory/{id}")
    public String disableCategory(@PathVariable("id") int id) {

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


    @GetMapping("/dashboard")
    public String dashboard() {

        return "Admin/dashboard";
    }


}
