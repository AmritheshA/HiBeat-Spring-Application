package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Servicess.Admin_Service.AdminDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final AdminDashboardService adminDashboardService;

    @Autowired
    public AdminController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return adminDashboardService.dashboard(model);
    }


}
