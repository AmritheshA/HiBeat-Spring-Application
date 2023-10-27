package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.OrderProducts;
import com.Hibeat.Hibeat.Model.Orders;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.sample_pojo;
import com.Hibeat.Hibeat.Repository.CartRepository;
import com.Hibeat.Hibeat.Repository.OrderProductRepository;
import com.Hibeat.Hibeat.Repository.OrderRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminOrderController {


    CartRepository cartRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderProductRepository orderProductRepository;

    @Autowired
    public AdminOrderController(CartRepository cartRepository, UserRepository userRepository, OrderRepository orderRepository, OrderProductRepository orderProductRepository) {

        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
    }


    @GetMapping("/orders")
    public String orders(Model model, Principal principal) {

        try {
            User user = userRepository.findByName(principal.getName());
            List<Orders> orders = orderRepository.findAll();
            List<OrderProducts> orderProducts = orderProductRepository.findAll();

            model.addAttribute("orders", orderProducts);


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error-page";
        }
        return "Admin/order";
    }


    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(
            @RequestParam("orderId") String orderId,
            @RequestParam("selecteds") String status) {

        try {


            Orders order = orderRepository.findByOrderId(orderId);

            if (status.equals("Delivered")) {
                order.setDeliveredDate(dateFinder(0));
                order.setReturnExpiryDate(dateFinder(7));
            } else if (status.equals("Cancelled")) {
                order.setCancelled(true);
            }

            order.setStatus(status);
            orderRepository.save(order);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Error");
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Success");

    }


    public LocalDate dateFinder(int numOfDates) {
        ZonedDateTime kolkataTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime date = kolkataTime.plusDays(numOfDates);

        return date.toLocalDate();
    }
    @GetMapping("/order-details")
    public String orderDetails() {
        return "Admin/order-details";
    }
    @GetMapping("/singleOrderDetails")
    public String orderDetailss(@RequestParam("orderId") String orderId,Model model,Principal principal){


        Orders orders = orderRepository.findByOrderId(orderId);
        User user = orders.getUser();

        model.addAttribute("orders" ,orders);
        model.addAttribute("address",user.getAddresses().get(orders.getAddressIndex()));

        log.info("inside the singleOrderDetails");


        return "Admin/order-details";
    }

}
