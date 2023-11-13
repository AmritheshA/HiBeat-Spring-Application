package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.User.OrderProducts;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.Servicess.User_Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminOrderServiceImp implements AdminOrderService {

    private final OrderService orderService;

    @Autowired
    public AdminOrderServiceImp(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String orders(Model model, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Page<OrderProducts> orderProductsPage = orderService.findAll(pageable);

            List<OrderProducts> orderProducts = orderProductsPage.getContent();

            // Add the data to the model
            model.addAttribute("orders", orderProducts);
            model.addAttribute("totalPages", orderProductsPage.getTotalPages());

        } catch (Exception e) {
            log.info("orders" + e.getMessage());
            return "Exception/404";
        }
        return "Admin/order";
    }

    @Override
    public ResponseEntity<String> updateOrderStatus(String orderId, String status) {
        try {
            Orders order = orderService.findByOrderId(orderId);

            if (status.equals("Delivered")) {
                order.setDeliveredDate(dateFinder(0));
                order.setReturnExpiryDate(dateFinder(7));
                order.getPayments().setStatus("Paid");
                order.getPayments().setPaymentTime(dateFinder(0).toString());
            } else if (status.equals("Cancelled")) {
                order.setCancelled(true);
            }

            order.setStatus(status);
            orderService.save(order);
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Success");

        } catch (Exception e) {
            log.info("updateOrderStatus" + e.getMessage());
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Error");
        }
    }

    @Override
    public String orderDetails(String orderId, Model model) {
        try {
            Orders orders = orderService.findByOrderId(orderId);
            User user = orders.getUser();

            model.addAttribute("orders", orders);
            model.addAttribute("address", user.getAddresses().get(orders.getAddressIndex()));

            log.info("inside the singleOrderDetails");

            return "Admin/order-details";
        }catch (Exception e){
            log.info("orderDetails"+e.getMessage());
            return "Exception/404";
        }

    }


    public LocalDate dateFinder(int numOfDates) {
        ZonedDateTime kolkataTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime date = kolkataTime.plusDays(numOfDates);

        return date.toLocalDate();
    }
}
