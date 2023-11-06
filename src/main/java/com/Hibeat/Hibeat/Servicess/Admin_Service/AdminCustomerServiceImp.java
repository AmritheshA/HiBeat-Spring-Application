package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Servicess.User_Service.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Slf4j
public class AdminCustomerServiceImp implements AdminCustomerService {

    private final UserServices userServices;

    @Autowired
    public AdminCustomerServiceImp(UserServices userServices) {
        this.userServices = userServices;
    }

    @Override
    public String customers(Model model) {

        try {
            List<User> users = userServices.findAll(Sort.by(Sort.Direction.ASC, "id"));

            if (!(users.isEmpty())) {
                model.addAttribute("users", users);
            }
            return "Admin/customers";
        } catch (Exception e) {
            log.info("customers" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String blockCustomer(int userId) {
        User user = userServices.findAllById(userId);
        try {
            if (user != null && user.getStatus().equals("UN-BLOCKED")) {
                user.setStatus("BLOCKED");
                userServices.save(user);
            } else if (user != null && user.getStatus().equals("BLOCKED")) {
                user.setStatus("UN-BLOCKED");
                userServices.save(user);
            }
            return "redirect:/admin/customers";
        } catch (Exception e) {
            log.info("blockCustomer" + e.getMessage());
            return "Exception/404";
        }
    }
}
