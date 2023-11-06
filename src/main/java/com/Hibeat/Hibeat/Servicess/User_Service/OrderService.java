package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.OrderProducts;
import com.Hibeat.Hibeat.Model.Orders;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Order_DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;


public interface OrderService {
    ResponseEntity<String> orders(Order_DTO orderDetails);
    String checkOut(Model model);

    String orderDetails(String orderId,Model model);

    Orders findByOrderId(String orderId);

    String myOrder(Model model);

    String cancelOrder(String orderId);
    Page<OrderProducts> findAll(Pageable pageable);

    Orders save(Orders order);

}