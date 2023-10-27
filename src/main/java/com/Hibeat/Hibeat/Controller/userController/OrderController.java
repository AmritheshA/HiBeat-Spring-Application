package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Order_DTO;
import com.Hibeat.Hibeat.Repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@Slf4j
@Transactional
public class OrderController {

    UserRepository userRepository;
    CartRepository cartRepository;
    OrderRepository orderRepository;
    PaymentRepository paymentRepository;
    ProductRepository productRepository;
    OrderProductRepository orderProductRepository;

    @Autowired
    public OrderController(UserRepository userRepository,
                           CartRepository cartRepository,
                           OrderRepository orderRepository,
                           PaymentRepository paymentRepository,
                           ProductRepository productRepository,
                           OrderProductRepository orderProductRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;

    }

    @PostMapping("/orders")
    public ResponseEntity<String> checkOut(@RequestBody Order_DTO orderDetails
            , Principal principal) {


        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());
        Orders orders = new Orders();

//      mapping cartProduct into orderProduct --->
        List<OrderProducts> orderProductList = productMapping(user.getId(), orders);

        double cartTotalAmount = cart.getTotalCartAmount();

//        Setting and getting payment object  -->


//        Setting values to the order entity....
        orders.setUser(user);
        orders.setAddressIndex(orders.getAddressIndex());
        orders.setOrderProducts(orderProductList);
        orders.setOrderDate(dateFinder(0));
        orders.setTotalAmount(cartTotalAmount);


        Payments payment = payment(orderDetails.getPaymentMethod(), principal.getName(), orders);
        orders.setPayments(payment);
        paymentRepository.save(payment);
        orderRepository.save(orders);
//        Stock Reducing --->
        stockReducer(user.getId());
        orders.setOrderId(generateUniqueOrderId(orders));

        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Success");

    }

    @GetMapping("/my-orders")
    public String myOrders(Principal principal, Model model) {

        String userName = principal.getName();

        User user = userRepository.findByName(userName);

        List<Orders> order = user.getOrders();


        List<OrderProducts> allProducts = order.stream()
                .flatMap(orders -> orders.getOrderProducts().stream())
                .toList();

        if (allProducts !=null) {
            model.addAttribute("orderProductss", allProducts);
        } else {
            model.addAttribute("orderProductss",new ArrayList<OrderProducts>());
        }

        return "User/orders";
    }


    //    For generating Unique random orderId..
    public static String generateUniqueOrderId(Orders currentOrder) {
        final String ALLOWED_CHARACTERS = "ABCDMZabcdestuvwxyz0724589-";

        SecureRandom random = new SecureRandom();
        String uniqueId;
        StringBuilder stringBuilder = new StringBuilder(13);

        for (int i = 0; i < 13; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        uniqueId = "#" + stringBuilder.toString() + currentOrder.getOrdersId();

        return uniqueId;
    }

    public LocalDate dateFinder(int numOfDates) {
        ZonedDateTime kolkataTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime date = kolkataTime.plusDays(numOfDates);

        return date.toLocalDate();
    }

    public List<OrderProducts> productMapping(Integer userId, Orders orders) {

//        productMapping from CartProduct To OrderProducts;
        Cart cart = cartRepository.findByUserId(userId);


        return cart.getCartProducts().stream().map(cartProduct -> {
            OrderProducts orderProducts = new OrderProducts();
            orderProducts.setProduct(cartProduct.getProduct());
            orderProducts.setQuantity(cartProduct.getQuantity());
            orderProducts.setOrders(orders);
            return orderProducts;
        }).toList();


    }

    public void stockReducer(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            for (CartProduct cartProduct : cart.getCartProducts()) {
                Products product = cartProduct.getProduct();
                int quantity = cartProduct.getQuantity();
                int currentStock = product.getStock();

                if (currentStock >= quantity) {
                    product.setStock(currentStock - quantity);
                    productRepository.save(product); // Assuming you're using Spring Data JPA
                } else {
                    log.info("Product Is Out Of Stock And Users Are Try To Access Product ?? cant reduce the stock");
                    return;
                }
            }
        }
    }

    public Payments payment(String paymentMethod, String userName, Orders orders) {

        Payments payment = new Payments();

        User user = userRepository.findByName(userName);
        Cart cart = cartRepository.findByUserId(user.getId());

        double cartTotalAmount = cart.getTotalCartAmount();


        if (paymentMethod != null && !paymentMethod.equals("Razorpay")) {
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(cartTotalAmount);
            payment.setOrders(orders);

        } else {
//            RazorPay
        }

        return payment;
    }

}