package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Servicess.Admin_Service.AdminCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customers")
@Slf4j
public class CustomerController {
    private final AdminCustomerService adminCustomerService;

    @Autowired
    public CustomerController(AdminCustomerService adminCustomerService) {
        this.adminCustomerService = adminCustomerService;
    }

    @GetMapping("")
    public String customers(Model model) {
      return adminCustomerService.customers(model);
    }

    @GetMapping("/block-user/{id}")
    public String blockUser(@PathVariable("id") int userId) {
        return adminCustomerService.blockCustomer(userId);
    }

}
