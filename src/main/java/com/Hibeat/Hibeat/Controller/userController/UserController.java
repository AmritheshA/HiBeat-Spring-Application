package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.FilterDataRequest;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import com.Hibeat.Hibeat.Repository.User.OrderProductRepository;
import com.Hibeat.Hibeat.Servicess.User_Service.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
public class UserController {

    private final UserServices userServices;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserController(UserServices userServices, OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.userServices = userServices;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Products> products = orderProductRepository.findTopSellingProducts();
        if(products.size() < 4){
            products = productRepository.findAll();
        }
        model.addAttribute("sliders",userServices.allBanners());
        model.addAttribute("topProduct",products.stream().limit(4));
        model.addAttribute("newProducts",productRepository.findAll().stream().limit(4));
        return "User/home";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "searchKey", required = false) String searchKey,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "value", required = false) String value,
                       Model model) {
        return userServices.shopPage(searchKey, model, type, value);
    }


    @PostMapping("user/filter")
    public ResponseEntity<List<Product_DTO>> filterProducts(@RequestBody(required = false) FilterDataRequest filterDataRequest) {
        try {

            // Access your filter data using filterDataRequest.getFilterData()
            Map<String, List<String>> filterData = filterDataRequest.getFilterData();

            // Continue with your service logic...
            List<Product_DTO> filteredProducts = userServices.filterProducts(filterData,filterDataRequest.getStatus());

            return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ModelAttribute("userName")
    public String getUserName( ) {
        if (!(userServices.currentUserName().equals("anonymousUser"))) {
            return userServices.currentUserName();
        }
        return "Login";
    }

   @GetMapping("user/cart-count")
   public Integer getCount(Model model) {
       log.info("error at cart");
        model.addAttribute("cartCount",userServices.totalCartCount());
       return userServices.totalCartCount();
   }
    @ModelAttribute("cartCount")
    public Integer getCartCount( ) {
        return userServices.totalCartCount();

    }
    @ModelAttribute("wishlistCount")
    public Integer getWishListCounts( ) {
        log.info("error");
        return userServices.totalWishlistCount();

    }
    @GetMapping("user/wishlist-count")
    public Integer getWishlistCount(Model model) {
        log.info("error");
        model.addAttribute("cartCount",userServices.totalWishlistCount());
        return userServices.totalWishlistCount();
    }

    @GetMapping("user/product-details/{id}")
    public String productDetails(@PathVariable("id") int id, Model model) {
       return userServices.productDetails(id,model);
    }

    @GetMapping("user/sample")
    public String sample() {
        return "sam";
    }

}
