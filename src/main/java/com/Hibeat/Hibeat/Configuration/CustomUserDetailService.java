package com.Hibeat.Hibeat.Configuration;

import com.Hibeat.Hibeat.Model.Admin.Admin;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.ModelMapper.ModelMapperConverter;
import com.Hibeat.Hibeat.Repository.Admin.AdminRepository;
import com.Hibeat.Hibeat.Repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ModelMapperConverter modelMapperConverter;


    @Autowired
    public CustomUserDetailService(UserRepository userRepository, AdminRepository adminRepository, ModelMapperConverter modelMapperConverter) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.modelMapperConverter = modelMapperConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByName(username);
        Admin admin = adminRepository.findByAdminName(username);

        if(user != null && user.getStatus().equals("UN-BLOCKED")) {

            return new CustomUserDetails(modelMapperConverter.userToDTO(user));
        }
        else if (admin != null) {

            return new CustomUserDetails(modelMapperConverter.adminToDTO(admin));
        }else{

        }
        throw  new UsernameNotFoundException("User Not Found");
    }
}
