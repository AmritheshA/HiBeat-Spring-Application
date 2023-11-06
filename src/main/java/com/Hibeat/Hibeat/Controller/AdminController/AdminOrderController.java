package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Servicess.Admin_Service.AdminOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminOrderController {

    private final AdminOrderService orderService;
    @Autowired
    public AdminOrderController( AdminOrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/orders")
    public String orders(Model model,
                         @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                         @RequestParam(value = "size", defaultValue = "4", required = false) int size) {
        return orderService.orders(model, page, size);
    }


    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestParam("orderId") String orderId,
                                                    @RequestParam("selecteds") String status) {
        return orderService.updateOrderStatus(orderId,status);
    }
    @GetMapping("/order-details")
    public String orderDetails() {
        return "Admin/order-details";
    }

    @GetMapping("/singleOrderDetails")
    public String orderDetailes(@RequestParam("orderId") String orderId, Model model) {
        return orderService.orderDetails(orderId,model);
    }

}
