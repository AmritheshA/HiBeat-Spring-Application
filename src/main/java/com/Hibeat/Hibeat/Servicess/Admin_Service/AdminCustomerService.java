package com.Hibeat.Hibeat.Servicess.Admin_Service;

import org.springframework.ui.Model;

public interface AdminCustomerService {
    String customers(Model model);

    String blockCustomer(int userId);

}

