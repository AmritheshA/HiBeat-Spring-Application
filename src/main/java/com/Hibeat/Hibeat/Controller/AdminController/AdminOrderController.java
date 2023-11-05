package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Model.OrderProducts;
import com.Hibeat.Hibeat.Model.Orders;
import com.Hibeat.Hibeat.Model.User;
import com.Hibeat.Hibeat.Repository.CartRepository;
import com.Hibeat.Hibeat.Repository.OrderProductRepository;
import com.Hibeat.Hibeat.Repository.OrderRepository;
import com.Hibeat.Hibeat.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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
    public String orders(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                         @RequestParam(value = "size", defaultValue = "4", required = false) int size) {
        try {
            User user = userRepository.findByName(principal.getName());

            Pageable pageable = PageRequest.of(page, size);

            // Fetch the data, for example using your orderProductRepository
            Page<OrderProducts> orderProductsPage = orderProductRepository.findAll(pageable);

            // Get the list of order products from the page
            List<OrderProducts> orderProducts = orderProductsPage.getContent();

            // Add the data to the model
            model.addAttribute("orders", orderProducts);
            model.addAttribute("totalPages", orderProductsPage.getTotalPages());

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
