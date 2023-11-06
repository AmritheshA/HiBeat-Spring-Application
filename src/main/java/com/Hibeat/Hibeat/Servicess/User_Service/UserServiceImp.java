package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Cart;
import com.Hibeat.Hibeat.Model.Orders;
import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.CartRepository;
import com.Hibeat.Hibeat.Repository.OrderRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Slf4j
public class UserServiceImp implements UserServices {


    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;

    private final ModelMapperConverter modelMapperConverter;



    @Autowired
    public UserServiceImp(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository, ModelMapperConverter modelMapperConverter, PasswordEncoder passwordEncoder, ProductService productService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productService = productService;
    }

    public String currentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public String shopPage(String searchKey, Model model) {
        if (searchKey == null) {
            List<Products> products1 = productService.findAllProduct();

            List<Products> products = products1.stream().filter(products2 -> products2.getStatus().equals("ACTIVE")).toList();

            model.addAttribute("products", products);
        } else {
            List<Products> productsList = productService.searchProductByName(searchKey);

            if (!(productsList.isEmpty())) {
                model.addAttribute("products", productsList);
            }
        }
        return "User/shop";
    }

    @Override
    public String productDetails(int id, Model model) {
        try {
        Products product = productService.findAllById(id);

        int categoryId = product.getCategories();

        model.addAttribute("product", product);

        if (categoryId != 0) {
            List<Products> products = productService.findByCategories(categoryId);
            List<Products> limitedProducts = products.stream().limit(3).toList();

            model.addAttribute("relatedProduct", limitedProducts);
        }

        return "User/productDetails";
        }catch (Exception e){
            log.info("productDetails"+e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Override
    public User saveUser(DTO userDetails) {

        User userInfo = modelMapperConverter.DTOToUser(userDetails);
//        Before saving Encoding the password
        userInfo.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        return userRepository.save(userInfo);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByName(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public User findAllById(Integer productId) {
        return userRepository.findAllById(productId);
    }

    @Override
    public List<User> findByNameContaining(String keyword) {
        return userRepository.findByNameContaining(keyword);
    }

    @Override
    public Cart save_cart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Orders save_orders(Orders orders) {
        return orderRepository.save(orders);
    }

//    public List<Products> findAllByIsdIn(List<Integer> productIds) {
//        return productRepository.findAllByIdIn(productIds);
//    }
    @Override
    public List<Orders> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

}
