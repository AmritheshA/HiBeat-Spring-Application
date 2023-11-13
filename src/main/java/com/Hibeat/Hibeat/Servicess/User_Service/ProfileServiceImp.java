package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Configuration.CustomUserDetailService;
import com.Hibeat.Hibeat.Model.Admin.Address;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.AddressDTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.RestPasswordDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProfileServiceImp implements ProfileService {

    private final UserServices userServices;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileServiceImp(UserServices userServices, CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder) {
        this.userServices = userServices;
        this.customUserDetailService = customUserDetailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String getProfile(Model model) {
        try {

            User user = userServices.findByName(userServices.currentUserName());
            if (user != null) {
                model.addAttribute("user", user);
            }
            return "User/profile";

        } catch (Exception e) {
            log.info("getProfile" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String updateProfile(String username, String email, String mobile) {
        try {


            User user = userServices.findByName(userServices.currentUserName());

            user.setName(username);
            user.setEmail(email);
            user.setMobile(mobile);
            userServices.save(user);

//        I use principle.getName for fetching user Details,
//        so i update the current security context principle with updated userDetails
            UserDetails updatedUserDetails = customUserDetailService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, null, updatedUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);

            return "redirect:/user/profile";
        } catch (Exception e) {
            log.info("updateProfile" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public ResponseEntity<String> restPassword(RestPasswordDTO restPasswordDTO) {
        try {
            User user = userServices.findByName(userServices.currentUserName());

            if (passwordEncoder.matches(restPasswordDTO.getOldPassword(), user.getPassword())) {

                user.setPassword(passwordEncoder.encode(restPasswordDTO.getNewPassword()));
                // Save the updated password
                userServices.save(user);
                return ResponseEntity.ok().body("success");
            } else {
                return ResponseEntity.ok().body("not-match");
            }
        } catch (Exception e) {
            log.info("restPassword" + e.getMessage());
            return ResponseEntity.ok().body("error");
        }
    }

    @Override
    public String myAddress(Model model) {

        User user = userServices.findByName(userServices.currentUserName());

        List<Address> addresses = user.getAddresses();

        model.addAttribute("addresses", addresses);

        return "User/address";
    }

    @Override
    public String updateAddress(int index, Model model) {
        try {
            User user = userServices.findByName(userServices.currentUserName());

            model.addAttribute("address", user.getAddresses().get(index));
            model.addAttribute("address_index", index);

            return "User/updateAddress";
        } catch (Exception e) {
            log.info("updateAddress" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String newAddress(AddressDTO address) {
        try {
            User user = userServices.findByName(userServices.currentUserName());

            user.setMobile(address.getMobile());
            Address addressDetails = addressManagement(address);

            if (user.getAddresses() == null) {
                user.setAddresses(new ArrayList<>());
            }

            user.getAddresses().add(addressDetails);
            userServices.save(user);

            return "redirect:/user/checkout";
        } catch (Exception e) {
            log.info("newAddress" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String update_address(int addressIndex, AddressDTO address) {

        try {
            User user = userServices.findByName(userServices.currentUserName());

            Address addressDetails = addressManagement(address);

            user.setMobile(address.getMobile());
            user.getAddresses().set(addressIndex, addressDetails);

            userServices.save(user);

            return "redirect:/user/checkout";
        } catch (Exception e) {
            log.info("update_address" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String removeAddress(int index) {
        try {
            User user = userServices.findByName(userServices.currentUserName());

            user.getAddresses().remove(index);
            userServices.save(user);

            return "redirect:/user/checkout";
        }catch (Exception e){
            log.info("removeAddress"+e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String removeMyAddress(int index) {
        try {
            User user = userServices.findByName(userServices.currentUserName());
            user.getAddresses().remove(index);
            userServices.save(user);
            return "redirect:/user/profile/my-address";
        }catch (Exception e){
            log.info("removeMyAddress"+e.getMessage());
            return "Exception/404";
        }
    }

    public Address addressManagement(AddressDTO address) {

        Address addressDetails = new Address();
        addressDetails.setAddress(address.getAddress());
        addressDetails.setName(address.getName());
        addressDetails.setLocality(address.getLocality());
        addressDetails.setCity(address.getCity());
        addressDetails.setPin(address.getPin());
        addressDetails.setMobile(address.getMobile());
        return addressDetails;
    }

}
