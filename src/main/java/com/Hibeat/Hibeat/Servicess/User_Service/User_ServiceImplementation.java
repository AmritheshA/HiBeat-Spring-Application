package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin;
import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.AdminRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import com.Hibeat.Hibeat.Servicess.User_Service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class User_ServiceImplementation implements Services {



    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapperConverter modelMapperConverter;
    @Autowired
    public User_ServiceImplementation(UserRepository userRepository,
                                      ModelMapperConverter modelMapperConverter,
                                      PasswordEncoder passwordEncoder) {
        this.modelMapperConverter = modelMapperConverter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(DTO userDetails) {

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


}
