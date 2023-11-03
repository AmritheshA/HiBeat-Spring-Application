package com.Hibeat.Hibeat.Controller.userController;

import com.Hibeat.Hibeat.Model.*;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Order_DTO;
import com.Hibeat.Hibeat.Repository.*;
import com.Hibeat.Hibeat.Servicess.User_Service.TwilioService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@Slf4j
@Transactional
public class OrderController {

    UserRepository userRepository;
    CartRepository cartRepository;
    CartProductRepository cartProductRepository;
    OrderRepository orderRepository;
    PaymentRepository paymentRepository;
    ProductRepository productRepository;
    OrderProductRepository orderProductRepository;
    WalletRepository walletRepository;
    WalletHistoryRepository walletHistoryRepository;
    CouponRepository couponRepository;
    TwilioService twilioService;

    @Autowired
    public OrderController(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository, PaymentRepository paymentRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, CartProductRepository cartProductRepository, TwilioService twilioService, WalletRepository walletRepository, WalletHistoryRepository walletHistoryRepository, CouponRepository couponRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.cartProductRepository = cartProductRepository;
        this.twilioService = twilioService;
        this.walletRepository = walletRepository;
        this.walletHistoryRepository = walletHistoryRepository;
        this.couponRepository = couponRepository;


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

        uniqueId = "#" + stringBuilder + currentOrder.getOrdersId();

        return uniqueId;
    }

    @GetMapping("/checkout")
    public String checkOut(Principal principal, Model model) {
        try {

            String userName = principal.getName();
            User user = userRepository.findByName(userName);
            Cart cart = cartRepository.findByUserId(user.getId());
            Coupons coupons = couponRepository.findByCouponCode(cart.getUsedCoupon());

            if (coupons == null) {
                double discountAmount = 0.0;
            } else {
                double discountAmount = coupons.getDiscountAmount();
            }
            List<CartProduct> cartProducts = cart.getCartProducts();

            if (cartProducts.isEmpty()) {
                return "Exception/CartIsEmpty";
            }

            model.addAttribute("cartProducts", cartProducts);
            model.addAttribute("addresses", user.getAddresses());
            model.addAttribute("totalAmount", cart.getTotalCartAmount());
            model.addAttribute("walletAmount", user.getWallet().getWalletTotalAmount());

            if (coupons != null) {
                if (coupons.getStatus().equals("ACTIVE") && coupons.getSingleOrMultiple().equals("multiple") ||
                        coupons.getSingleOrMultiple().equals("single") && coupons.getUserid().isEmpty()) {

                    model.addAttribute("discountAmount", coupons.getDiscountAmount());

                } else {
                    model.addAttribute("discountAmount", 0.0);
                }
            }else{
                model.addAttribute("discountAmount", 0.0);
            }

        } catch (Exception e) {
            System.out.println(e);
            return "Exception/CartIsEmpty";
        }

        return "User/checkout";
    }

    @PostMapping("/orders")
    public ResponseEntity<String> checkOut(@RequestBody Order_DTO orderDetails, Principal principal) {

        User user = userRepository.findByName(principal.getName());
        Cart cart = cartRepository.findByUserId(user.getId());
        Orders orders = new Orders();

//      mapping cartProduct into orderProduct --->
        List<OrderProducts> orderProductList = productMapping(user.getId(), orders);

        double cartTotalAmount = cart.getTotalCartAmount();

//        Setting values to the order entity....
        orders.setUser(user);
        orders.setAddressIndex(orders.getAddressIndex());
        orders.setOrderProducts(orderProductList);
        orders.setOrderDate(dateFinder(0));
        orders.setTotalAmount(cartTotalAmount);

        if (!(orderDetails.getPaymentMethod().equals("cashOnDelivery"))) {
            orders.setAmountStatus("Pending");
        } else {
            orders.setAmountStatus("Paid");
        }


        Payments payment = payment(orderDetails.getPaymentMethod(), principal.getName(), orderDetails.getPaymentId(), orders);
        orders.setPayments(payment);
        paymentRepository.save(payment);
        orderRepository.save(orders);
//        Stock Reducing ---->
        stockManager(user.getId(), 0);
        orders.setOrderId(generateUniqueOrderId(orders));

//        clearing cart after all process
        cart.setTotalCartAmount(0.0);
        cart.getCartProducts().clear();


        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Success");

    }

    @GetMapping("/my-orders")
    public String myOrders(Principal principal, Model model) {

        String userName = principal.getName();
        User user = userRepository.findByName(userName);

        List<Orders> order = user.getOrders();


        List<OrderProducts> allProducts = order.stream().flatMap(orders -> orders.getOrderProducts().stream()).toList();

        if (allProducts != null) {
            model.addAttribute("orderProductss", allProducts);

        } else {
            model.addAttribute("orderProductss", new ArrayList<OrderProducts>());
        }

        return "User/orders";
    }

    @GetMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") String orderId, Principal principal) {

        try {


            User user = userRepository.findByName(principal.getName());
            Orders orders = orderRepository.findByOrderId(orderId);

            if (orders.getPayments().getPaymentMethod().equals("cashOnDelivery") && !(orders.getStatus().equals("Delivered") || orders.getStatus().equals("Return"))) {
                orders.setCancelled(true);
                stockManager(user.getId(), 1);
                orders.setStatus("Cancelled");
            } else if (!(orders.getPayments().getPaymentMethod().equals("cashOnDelivery")) && !(orders.getStatus().equals("Delivered") || orders.getStatus().equals("Return"))) {

                Wallet wallet = user.getWallet();
                List<WalletHistory> walletHistories;

                if (wallet == null) {
                    wallet = new Wallet();
                    walletHistories = new ArrayList<>();
                } else {
                    walletHistories = wallet.getWalletHistory();
                }

                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setAddedAmount(orders.getTotalAmount());
                walletHistory.setDepositOrWithdraw("Deposit");
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                walletHistory.setAmountAddedTime(currentDateTime.format(formatter));
                walletHistory.setWallet(wallet);

                walletHistories.add(walletHistory);

                double totalAmount = walletHistories.stream().mapToDouble(addedAmount -> addedAmount.getAddedAmount()).sum();

                log.info("User TotalWalletAmount" + totalAmount); // Log the user's total wallet amount

                wallet.setWalletTotalAmount(totalAmount);

                stockManager(user.getId(), 1);
                orders.setStatus("Cancelled");
                orderRepository.save(orders);


                wallet.setWalletHistory(walletHistories);
                wallet.setUser(user);
                walletRepository.save(wallet);


            }
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/error-page";

        }

        return "redirect:/user/my-orders";
    }

    public LocalDate dateFinder(int numOfDates) {
        ZonedDateTime kolkataTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime date = kolkataTime.plusDays(numOfDates);

        return date.toLocalDate();
    }

    public List<OrderProducts> productMapping(Integer userId, Orders orders) {

//        productMapping from CartProduct To OrderProducts;
        Cart cart = cartRepository.findByUserId(userId);

        List<OrderProducts> orderProductsList = cart.getCartProducts().stream().map(cartProduct -> {
            OrderProducts orderProducts = new OrderProducts();
            orderProducts.setProduct(cartProduct.getProduct());
            orderProducts.setQuantity(cartProduct.getQuantity());
            orderProducts.setOrders(orders);
            return orderProducts;
        }).toList();


        return orderProductsList;

    }

    public void stockManager(Integer userId, int increaseOrDecrease) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            for (CartProduct cartProduct : cart.getCartProducts()) {
                Products product = cartProduct.getProduct();
                int quantity = cartProduct.getQuantity();
                int currentStock = product.getStock();

                if (increaseOrDecrease == 0) {

                    if (currentStock >= quantity) {
                        product.setStock(currentStock - quantity);
                        productRepository.save(product);

                        if (currentStock < 25) {
//                        sending a sms for the admin/seller to notify product reached the limit
                            twilioService.twilioSMS(product.getName());
                        }
                    } else {
                        log.info("Product Is Out Of Stock");
                        return;
                    }
                } else if (increaseOrDecrease == 1) {

                    product.setStock(currentStock + quantity);
                    productRepository.save(product);
                }
            }
        }
    }

    public Payments payment(String paymentMethod, String userName, String paymentId, Orders orders) {

        Payments payment = new Payments();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        User user = userRepository.findByName(userName);
        Cart cart = cartRepository.findByUserId(user.getId());

        double cartTotalAmount = cart.getTotalCartAmount();


        if (paymentMethod != null) {
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(cartTotalAmount);
            payment.setOrders(orders);
            payment.setPaymentsId(paymentId);

            if (paymentMethod.equals("PayPal") || paymentMethod.equals("RazorPay")) {

                payment.setPaymentTime(currentDateTime.format(formatter));

            } else if (paymentMethod.equals("wallet")) {
                Wallet wallet = walletRepository.findByUser(user);
                WalletHistory walletHistory = new WalletHistory();
                List<WalletHistory> walletHistories = wallet.getWalletHistory();

                if (wallet.getWalletTotalAmount() >= cartTotalAmount) {
                    wallet.setWalletTotalAmount(wallet.getWalletTotalAmount() - cartTotalAmount);

                    walletHistory.setWithdrawAmount(cartTotalAmount);
                    walletHistory.setAmountWithdrawTime(currentDateTime.format(formatter));
                    walletHistory.setDepositOrWithdraw("WithDraw");
                    walletHistory.setWallet(wallet);
                    walletHistories.add(walletHistory);
                    wallet.setWalletHistory(walletHistories);

                    walletRepository.save(wallet);

                }
            }


        }
        return payment;
    }
}