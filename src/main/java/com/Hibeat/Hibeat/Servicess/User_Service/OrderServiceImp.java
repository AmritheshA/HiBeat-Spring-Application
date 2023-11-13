package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Coupons;
import com.Hibeat.Hibeat.Model.Admin.Payments;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.*;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.Order_DTO;
import com.Hibeat.Hibeat.Repository.Admin.PaymentRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import com.Hibeat.Hibeat.Repository.User.OrderProductRepository;
import com.Hibeat.Hibeat.Repository.User.OrderRepository;
import com.Hibeat.Hibeat.Repository.User.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImp implements OrderService {

    private final UserServices userServices;
    private final CartService cartService;
    private final CouponService couponService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final TwilioService twilioService;
    private final OrderProductRepository orderProductRepository;


    @Autowired
    public OrderServiceImp(UserServices userServices, CartService cartService, CouponService couponService, ProductRepository productRepository, OrderRepository orderRepository, PaymentRepository paymentRepository, WalletRepository walletRepository, TwilioService twilioService, OrderProductRepository orderProductRepository) {
        this.userServices = userServices;
        this.cartService = cartService;
        this.couponService = couponService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.walletRepository = walletRepository;
        this.twilioService = twilioService;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public Orders findByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public String myOrder(Model model) {
        try {
            String userName = userServices.currentUserName();
            User user = userServices.findByName(userName);

            List<Orders> order = user.getOrders();


            List<OrderProducts> allProducts = order.stream().flatMap(orders -> orders.getOrderProducts().stream()).toList();

            if (allProducts != null) {
                model.addAttribute("orderProductss", allProducts);

            } else {
                model.addAttribute("orderProductss", new ArrayList<OrderProducts>());
            }

            return "User/orders";
        } catch (Exception e) {
            log.info("myOrder" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String cancelOrder(String orderId) {
        try {

            User user = userServices.findByName(userServices.currentUserName());
            Orders orders = orderRepository.findByOrderId(orderId);
            Wallet wallet = user.getWallet();


            if (orders.getPayments().getPaymentMethod().equals("cashOnDelivery") && !(orders.getStatus().equals("Delivered") || orders.getStatus().equals("Return"))) {
                orders.setCancelled(true);
                stockManager(user.getId(), 1);
                orders.setStatus("Cancelled");
            } else if (!(orders.getPayments().getPaymentMethod().equals("cashOnDelivery")) && !(orders.getStatus().equals("Delivered") || orders.getStatus().equals("Return"))) {

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

                double totalAmount = walletHistories.stream().mapToDouble(WalletHistory::getAddedAmount).sum();

                log.info("User TotalWalletAmount" + totalAmount); // Log the user's total wallet amount

                wallet.setWalletTotalAmount(totalAmount);

                stockManager(user.getId(), 1);
                orders.setStatus("Cancelled");


                wallet.setWalletHistory(walletHistories);
                wallet.setUser(user);


            }
            orderRepository.save(orders);
            walletRepository.save(wallet);


        } catch (Exception e) {
            log.info("cancelOrder" + e.getMessage());
            return "redirect:/error-page";

        }

        return "redirect:/user/my-orders";
    }

    @Override
    public Page<OrderProducts> findAll(Pageable pageable) {
        return orderProductRepository.findAll(pageable);
    }

    @Override
    public Orders save(Orders order) {
        return orderRepository.save(order);
    }

    @Override
    public String orderDetails(String orderId, Model model) {
        try {

            Orders orders = findByOrderId(orderId);
            User user = userServices.findByName(userServices.currentUserName());
            model.addAttribute("orders", orders);
            model.addAttribute("address", user.getAddresses().get(orders.getAddressIndex()));
            return "User/OrderDetails";
        } catch (Exception e) {
            log.info("orderDetails" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String checkOut(Model model) {
        try {

            String userName = userServices.currentUserName();
            User user = userServices.findByName(userName);
            Cart cart = cartService.findByUserId(user.getId());
            Coupons coupons = couponService.findByCouponCode(cart.getUsedCoupon());

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
            } else {
                model.addAttribute("discountAmount", 0.0);
            }

        } catch (Exception e) {
            log.info("checkOut" + e.getMessage());
            return "Exception/CartIsEmpty";
        }
        return "User/checkout";
    }

    @Override
    public ResponseEntity<String> orders(Order_DTO orderDetails) {
        try {
            User user = userServices.findByName(userServices.currentUserName());
            Cart cart = cartService.findByUserId(user.getId());
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

            Payments payment = payment(orderDetails.getPaymentMethod(), userServices.currentUserName(), orderDetails.getPaymentId(), orders);
            orders.setPayments(payment);
            paymentRepository.save(payment);
            orderRepository.save(orders);
            stockManager(user.getId(), 0);
            orders.setOrderId(generateUniqueOrderId(orders));

//        clearing cart after all process
            cart.setTotalCartAmount(0.0);
            cart.getCartProducts().clear();
            cart.setUsedCoupon(null);
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Success");
        } catch (Exception e) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("error");
        }
    }


    public List<OrderProducts> productMapping(Integer userId, Orders orders) {

//        productMapping from CartProduct To OrderProducts;
        Cart cart = cartService.findByUserId(userId);

        List<OrderProducts> orderProductsList = cart.getCartProducts().stream().map(cartProduct -> {
            OrderProducts orderProducts = new OrderProducts();
            orderProducts.setProduct(cartProduct.getProduct());
            orderProducts.setQuantity(cartProduct.getQuantity());
            orderProducts.setOrders(orders);
            return orderProducts;
        }).toList();


        return orderProductsList;

    }

    public LocalDate dateFinder(int numOfDates) {
        ZonedDateTime kolkataTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        ZonedDateTime date = kolkataTime.plusDays(numOfDates);

        return date.toLocalDate();
    }

    public String generateUniqueOrderId(Orders currentOrder) {
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

    public void stockManager(Integer userId, int increaseOrDecrease) {
        Cart cart = cartService.findByUserId(userId);

        if (cart != null) {
            for (CartProduct cartProduct : cart.getCartProducts()) {
                Products product = cartProduct.getProduct();
                int quantity = cartProduct.getQuantity();
                int currentStock = product.getStock();

                if (increaseOrDecrease == 0) {

                    if (currentStock >= quantity) {
                        product.setStock(currentStock - quantity);
                        productRepository.save(product);

//                        if (currentStock < 25) {
////                        sending a sms for the admin/seller to notify product reached the limit
//                            twilioService.twilioSMS(product.getName());
//                        }
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

        User user = userServices.findByName(userName);
        Cart cart = cartService.findByUserId(user.getId());


        double cartTotalAmount = cart.getTotalCartAmount();


        if (paymentMethod != null) {
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(cartTotalAmount);
            payment.setOrders(orders);
            payment.setPaymentsId(paymentId);

            if (paymentMethod.equals("PayPal") || paymentMethod.equals("RazorPay")) {
                payment.setStatus("Paid");
                orders.setAmountStatus("Paid");
                payment.setPaymentTime(currentDateTime.format(formatter));

            } else if (paymentMethod.equals("wallet")) {
                Wallet wallet = walletRepository.findByUser(user);
                WalletHistory walletHistory = new WalletHistory();
                List<WalletHistory> walletHistories = wallet.getWalletHistory();
                double walletTotalAmount = wallet.getWalletTotalAmount();
                payment.setStatus("Paid");
                orders.setAmountStatus("Paid");
                payment.setPaymentTime(currentDateTime.format(formatter));

                if (wallet.getWalletTotalAmount() >= cartTotalAmount) {
                    wallet.setWalletTotalAmount(walletTotalAmount - cartTotalAmount);

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
