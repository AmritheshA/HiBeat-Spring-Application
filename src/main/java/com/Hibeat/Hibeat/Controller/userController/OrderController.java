package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Order_DTO;
import com.Hibeat.Hibeat.Servicess.User_Service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@Transactional
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkOut(Model model) {
        return orderService.checkOut(model);
    }

    @GetMapping("/checkout/validation")
    public ResponseEntity<Boolean> checkOutValidation(Model model) {
        return orderService.checkOutValidation();
    }



    @GetMapping("/apply-coupon")
    public ResponseEntity<String> applyCoupon(@RequestParam("value") String couponCode){
        return orderService.applyCoupon(couponCode);
    }
    @GetMapping("/remove-coupon")
    public ResponseEntity<String> removeCoupon(){
        return orderService.removeCoupon();
    }

    @GetMapping("/orderDetails")
    public String orderDetails(@RequestParam("orderId") String orderId, Model model) {
        return orderService.orderDetails(orderId, model);
    }

    @PostMapping("/orders")
    public ResponseEntity<String> checkOut(@RequestBody Order_DTO orderDetails) {
        return orderService.orders(orderDetails);
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model) {
        return orderService.myOrder(model);
    }

    @GetMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") String orderId) {
        return orderService.cancelOrder(orderId);
    }

}