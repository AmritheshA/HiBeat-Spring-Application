package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.AddressDTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.RestPasswordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface ProfileService {
    String getProfile(Model model);

    String updateProfile(String username, String email, String mobile);

    ResponseEntity<String> restPassword(RestPasswordDTO restPasswordDTO);

    String myAddress(Model model);

    String updateAddress(int index, Model model);

    String newAddress(AddressDTO address);

    String update_address(int addressIndex, AddressDTO address);

    String removeAddress(int index);

    String removeMyAddress(int index);
}
