package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin;
import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Services {

    User save(DTO userInfo);

    User findByName(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    User findAllById(Integer productId);


    List<User> findByNameContaining(String keyword);


}
