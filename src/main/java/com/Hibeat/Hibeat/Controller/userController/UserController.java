package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Servicess.User_Service.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserServices userServices;

    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("sliders",userServices.allBanners());
        return "User/home";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "searchKey",required = false) String searchKey, Model model) {
     return userServices.shopPage(searchKey,model);
    }

    @ModelAttribute("userName")
    public String getUserName( ) {
        if (!(userServices.currentUserName().equals("anonymousUser"))) {
            return userServices.currentUserName();
        }
        return "Login";
    }

    @GetMapping("/product-details/{id}")
    public String productDetails(@PathVariable("id") int id, Model model) {
       return userServices.productDetails(id,model);
    }



    @GetMapping("/sample")
    public String sample() {

        return "sam";
    }


}
