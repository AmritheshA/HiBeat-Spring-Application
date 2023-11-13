package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Banner;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.Cart;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Model.User.Review;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.BannerRepository;
import com.Hibeat.Hibeat.Repository.User.CartRepository;
import com.Hibeat.Hibeat.Repository.User.OrderRepository;
import com.Hibeat.Hibeat.Repository.User.ReviewRepository;
import com.Hibeat.Hibeat.Repository.User.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImp implements UserServices {


    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;
    private final BannerRepository bannerRepository;
    private final ModelMapperConverter modelMapperConverter;


    @Autowired
    public UserServiceImp(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository, ModelMapperConverter modelMapperConverter, PasswordEncoder passwordEncoder, ProductService productService, ReviewRepository reviewRepository, BannerRepository bannerRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productService = productService;
        this.reviewRepository = reviewRepository;
        this.bannerRepository = bannerRepository;
    }

    public String currentUserName() {
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
            List<Review> reviews = reviewRepository.findByProducts(product);
            ArrayList<Integer> num = reviewRepository.findAllRating(product);
            double sum = reviews.stream().mapToDouble(Review ::getRating).sum();
            double adjustedAverageRating = Math.min(Math.max(sum/reviews.size(), 0), 5);


            List<Review> reviewList = reviews.stream().filter(review -> review.getProducts().getId().equals(id)).limit(5).toList();

            Map<Integer, Long> ratingCountMap = num.stream()
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

            Map<Integer, String> ratingPercentageMap = num.stream()
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> {
                                long count = entry.getValue();
                                return count > 0 ?
                                        String.format("%.0f%%", (count * 100.0) / num.size()) :
                                        "0%";
                            }
                    ));

            model.addAttribute("product", product);
            model.addAttribute("userId",currentUser().getId());
            model.addAttribute("reviews", reviewList);
            model.addAttribute("averageRating",adjustedAverageRating);
            model.addAttribute("totalReviews",reviews.size());
            model.addAttribute("starOne",ratingCountMap.get(1));
            model.addAttribute("starTwo",ratingCountMap.get(2));
            model.addAttribute("starThree",ratingCountMap.get(3));
            model.addAttribute("starFour",ratingCountMap.get(4));
            model.addAttribute("starFive",ratingCountMap.get(5));
            model.addAttribute("onePer",ratingPercentageMap.get(1));
            model.addAttribute("twoPer",ratingPercentageMap.get(2));
            model.addAttribute("threePer",ratingPercentageMap.get(3));
            model.addAttribute("fourPer",ratingPercentageMap.get(4));
            model.addAttribute("fivePer",ratingPercentageMap.get(5));



            if (categoryId != 0) {
                List<Products> products = productService.findByCategories(categoryId);
                List<Products> limitedProducts = products.stream().filter(products1 -> products1.getStatus().equals("ACTIVE") && products1.getId() != id).limit(4).toList();

                model.addAttribute("relatedProduct", limitedProducts);
            }

            return "User/productDetails";
        } catch (Exception e) {
            log.info("productDetails" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Override
    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userRepository.findByName(userName);
    }

    @Override
    public List<Banner> allBanners() {
        return bannerRepository.findAll();
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
