package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.AddressDTO;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.RestPasswordDTO;
import com.Hibeat.Hibeat.Servicess.User_Service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("")
    public String profile(Model model) {
        return profileService.getProfile(model);
    }

    @GetMapping("/updateProfile")
    public String updateProfile(@RequestParam("name") String username,@RequestParam("email") String email,@RequestParam("mobile") String mobile) {
        return profileService.updateProfile(username, email, mobile);
    }

    @PostMapping(value = "/resetUserPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetUserPassword(@RequestBody RestPasswordDTO restPasswordDTO){
        return profileService.restPassword(restPasswordDTO);
    }

    @GetMapping("/my-address")
    public String myAddress(Model model) {
        return profileService.myAddress(model);
    }

    @GetMapping("/new-address")
    public String newAddress() {
        return "User/NewAddress";
    }

    @GetMapping("/update-address")
    public String updateAddress(@RequestParam("index") int index, Model model) {
        return profileService.updateAddress(index,model);
    }

    @PostMapping("/new-address")
    public String newAddress(@ModelAttribute AddressDTO address) {
        return profileService.newAddress(address);
    }

    @PostMapping("/update-address")
    public String update_address(@ModelAttribute AddressDTO address,@RequestParam("index") int addressIndex) {
        return profileService.update_address(addressIndex,address);
    }

    @GetMapping("/remove-address")
    public String removeAdders(@RequestParam("index") int index) {
       return profileService.removeAddress(index);
    }

    @GetMapping("/removeAddress")
    public String removeAddresses(@RequestParam("index") int index) {
        return profileService.removeMyAddress(index);
    }

}