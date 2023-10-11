package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    ProductRepository productRepository;

    public UserController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/shop")
    public String shop(Model model) {

        List<Products> products = productRepository.findAll();

        model.addAttribute("products",products);

        return "User/shop";
    }

    @GetMapping("/product-details/{id}")
    public String productDetails(@PathVariable("id") int id,
                                 Model model){

        Products product = productRepository.findAllById(id);

        int categoryId = product.getCategories();

        if(product != null ){
            model.addAttribute("product",product);

            if(categoryId != 0){
                List<Products> products = productRepository.findByCategories(categoryId);
                List<Products> limitedProducts = products.stream().limit(3).toList();

                model.addAttribute("relatedProduct",limitedProducts);
            }
        }

        return "User/productDetails";
    }
}
