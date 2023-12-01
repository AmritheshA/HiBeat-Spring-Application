package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Banner;
import com.Hibeat.Hibeat.Model.Admin.Brands;
import com.Hibeat.Hibeat.Model.Admin.Categories;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.*;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.BannerRepository;
import com.Hibeat.Hibeat.Repository.Admin.BrandRepository;
import com.Hibeat.Hibeat.Repository.Admin.CategoryRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import com.Hibeat.Hibeat.Repository.User.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImp implements UserServices {


    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final BannerRepository bannerRepository;
    private final ModelMapperConverter modelMapperConverter;
    private final CategoryRepository categoryRepository;
    private final WishlistRepository wishlistRepository;
    private final BrandRepository brandRepository;


    @Autowired
    public UserServiceImp(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository, ModelMapperConverter modelMapperConverter, PasswordEncoder passwordEncoder, ProductRepository productRepository, ReviewRepository reviewRepository, BannerRepository bannerRepository, CategoryRepository categoryRepository, WishlistRepository wishlistRepository, BrandRepository brandRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.wishlistRepository = wishlistRepository;
        this.brandRepository = brandRepository;
    }

    public String currentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public String shopPage(String searchKey, Model model, String type, String value) {

        List<Categories> categories = categoryRepository.findAll();
        List<Brands> brands = brandRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);

        if (searchKey == null && type == null && value == null) {
            List<Products> products1 = productRepository.findAll();

            List<Products> products = products1.stream().filter(products2 -> products2.getStatus().equals("ACTIVE")).toList();

            model.addAttribute("products", products);
        } else if (type != null && value != null) {

            List<Products> filetProducts = filterByTypeValue(type, value);

            model.addAttribute("products", filetProducts);

        } else {
            List<Products> productsList = productRepository.findByNameContaining(searchKey);

            if (!(productsList.isEmpty())) {
                model.addAttribute("products", productsList);
            }
        }
        return "User/shop";
    }

    @Override
    public List<Product_DTO> filterProducts(Map<String, List<String>> filters, String status) {

        List<Products> result;

        log.info("status" + status);

        if (status == null) {
            result = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
                String key = entry.getKey();

                switch (key) {
                    case "Category":
                        List<String> categoryIdsAsString = entry.getValue();
                        for (String categoryName : categoryIdsAsString) {
                            List<Categories> categories = categoryRepository.findByCategoryName(categoryName);
                            for (Categories category : categories) {
                                List<Products> categoryProducts = productRepository.findByCategories(category.getId());
                                result.addAll(categoryProducts);
                            }
                        }
                        break;
                    case "Brand":
                        List<String> brandIdsAsString = entry.getValue();
                        for (String brandName : brandIdsAsString) {
                            List<Brands> brands = brandRepository.findByBrandName(brandName);
                            for (Brands brand : brands) {
                                List<Products> brandProducts = productRepository.findByBrand(brand.getId());
                                result.addAll(brandProducts);
                            }
                        }
                        break;
//
                case "Color":
                    List<String> colorIdsAsString = entry.getValue();
                    for (String color : colorIdsAsString) {
                        List<Products> colorProducts = productRepository.findByColour(color);
                            result.addAll(colorProducts);
                    }
                    break;
                }
            }
        } else {
            result = productRepository.findAll();
        }

        List<Products> uniqueProducts = result.stream()
                .distinct()
                .collect(Collectors.toList());

        return productsToProductDTO(uniqueProducts);
    }

    @Override
    public Integer totalCartCount() {
       Cart cart = cartRepository.findByUser(currentUser());
       if(cart == null){
           return 0;
       }
       return cart.getCartProducts().size();
    }

    @Override
    public Integer totalWishlistCount() {
        Wishlist wishlist = wishlistRepository.findByUser(currentUser());
        if(wishlist == null){
            return 0;
        }
        return wishlist.getWishlistItems().size();
    }


    public List<Product_DTO> productsToProductDTO(List<Products> productsList) {
        return productsList.stream()
                .filter(products -> "ACTIVE".equals(products.getStatus()))
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }


    private Product_DTO productToProductDTO(Products product) {

        Product_DTO productDTO = new Product_DTO();
        productDTO.setProductName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(Double.toString(product.getPrice()));
        productDTO.setId(product.getId());
        productDTO.setImages(product.getImages_path());

        return productDTO;
    }


    private List<Products> filterByTypeValue(String type, String value) {

        List<Products> productsList = null;

        if (type.equals("Category")) {

            productsList = productRepository.findByCategories(Integer.parseInt(value));
//        } else if (type.equals("Brand")) {
//
//            productService.searchProductByBrand(value);
//
        } else if (type.equals("Type")) {

        }

        return productsList;
    }

    @Override
    public String productDetails(int id, Model model) {
        try {
            Products product = productRepository.findAllById(id);
            Integer categoryId = product.getCategories();
            List<Review> reviews = reviewRepository.findByProducts(product);
            ArrayList<Integer> num = reviewRepository.findAllRating(product);
            double sum = reviews.stream().mapToDouble(Review::getRating).sum();
            double adjustedAverageRating = Math.min(Math.max(sum / reviews.size(), 0), 5);
            List<Orders> orders = orderRepository.findByUser(currentUser());
            boolean bought = false;
            for (Orders order : orders) {
                 bought = order.getOrderProducts().stream()
                        .anyMatch(orderProduct -> orderProduct.getProduct().equals(product));
            }


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
            model.addAttribute("userId", currentUser().getId());
            log.info("value"+bought);
            model.addAttribute("bought",bought);
            model.addAttribute("reviews", reviewList);
            model.addAttribute("averageRating", adjustedAverageRating);
            model.addAttribute("totalReviews", reviews.size());
            model.addAttribute("starOne", ratingCountMap.get(1));
            model.addAttribute("starTwo", ratingCountMap.get(2));
            model.addAttribute("starThree", ratingCountMap.get(3));
            model.addAttribute("starFour", ratingCountMap.get(4));
            model.addAttribute("starFive", ratingCountMap.get(5));
            model.addAttribute("onePer", ratingPercentageMap.get(1));
            model.addAttribute("twoPer", ratingPercentageMap.get(2));
            model.addAttribute("threePer", ratingPercentageMap.get(3));
            model.addAttribute("fourPer", ratingPercentageMap.get(4));
            model.addAttribute("fivePer", ratingPercentageMap.get(5));


            if (categoryId != 0) {
                List<Products> products = productRepository.findByCategories(categoryId);
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

    @Override
    public List<Orders> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

}
