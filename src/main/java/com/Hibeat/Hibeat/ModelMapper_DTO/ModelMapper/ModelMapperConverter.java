package com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper;

import com.Hibeat.Hibeat.Model.Admin.Admin;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.DTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Product_DTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperConverter {


    ModelMapper modelMapper;

    @Autowired
    public ModelMapperConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //    For User
    public DTO userToDTO(User user) {

        return modelMapper.map(user, DTO.class);
    }

    public User DTOToUser(DTO user) {
        if (user == null) {
            throw new UsernameNotFoundException("User Is Not Found To Convert UserDTO to user");
        }
        return modelMapper.map(user, User.class);
    }

    //    For admin
    public DTO adminToDTO(Admin admin) {

        return modelMapper.map(admin, DTO.class);
    }

    public Admin DTOToAdmin(DTO user) {
        if (user == null) {
            throw new UsernameNotFoundException("User Is Not Found To Convert UserDTO to user");
        }
        return modelMapper.map(user, Admin.class);
    }
//  For Products
    public Product_DTO ProductToDTO(Products products) {

        if (products == null) {
            throw new UsernameNotFoundException("User Is Not Found To Convert UserDTO to user");
        }
        return modelMapper.map(products, Product_DTO.class);
    }

    public Products DTOToProduct(Product_DTO dto) {
        if (dto == null) {
            throw new UsernameNotFoundException("User Is Not Found To Convert UserDTO to user");
        }
        return modelMapper.map(dto, Products.class);
    }
}
