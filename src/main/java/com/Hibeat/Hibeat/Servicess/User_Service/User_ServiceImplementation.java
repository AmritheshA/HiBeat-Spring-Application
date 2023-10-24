package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Cart;
import com.Hibeat.Hibeat.Model.Orders;
import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.CartRepository;
import com.Hibeat.Hibeat.Repository.OrderRepository;
import com.Hibeat.Hibeat.Repository.ProductRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class User_ServiceImplementation implements Services {


    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    private final ModelMapperConverter modelMapperConverter;

    @Autowired
    public User_ServiceImplementation(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository, OrderRepository orderRepository, ModelMapperConverter modelMapperConverter, PasswordEncoder passwordEncoder) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save_user(DTO userDetails) {

        User userInfo = modelMapperConverter.DTOToUser(userDetails);
//        Before saving Encoding the password
        userInfo.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        return userRepository.save(userInfo);
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

    public List<Products> findAllByIsdIn(List<Integer> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
    @Override
    public List<Orders> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

}
