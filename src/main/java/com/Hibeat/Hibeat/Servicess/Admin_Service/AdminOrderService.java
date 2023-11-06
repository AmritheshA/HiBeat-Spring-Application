package com.Hibeat.Hibeat.Servicess.Admin_Service;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface AdminOrderService {

    String orders(Model model, int page, int size);

    ResponseEntity<String> updateOrderStatus(String orderId, String status);

    String orderDetails(String orderId, Model model);

}
