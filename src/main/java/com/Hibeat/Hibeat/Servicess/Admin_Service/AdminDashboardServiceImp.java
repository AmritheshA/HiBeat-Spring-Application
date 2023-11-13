package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Repository.Admin.PaymentRepository;
import com.Hibeat.Hibeat.Repository.User.OrderProductRepository;
import com.Hibeat.Hibeat.Repository.User.OrderRepository;
import com.Hibeat.Hibeat.Repository.User.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.*;
import java.util.*;

@Service
@Slf4j
public class AdminDashboardServiceImp implements AdminDashboardService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;

    @Autowired
    public AdminDashboardServiceImp(PaymentRepository paymentRepository, OrderRepository orderRepository, UserRepository userRepository, OrderProductRepository orderProductRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
    }


    @Override
    public String dashboard(Model model) {
        try {

            Double totalSales = paymentRepository.totalSales();
            Integer soldProducts = orderRepository.totalSoldProducts();
            Integer activeCustomers = userRepository.totalUserCount();

            List<Products> topSellingProducts = topSelling();
            Map<String, Double> daily = dailySales();
            Map<String, Double> weekly = weeklySales();
            Map<String, Double> monthly = monthlySales();
            Map<String, Double> yearly = yearlySales();
            Map<String, Long> dailyCounts = dailySalesCount();
            Map<String, Long> weeklyCounts = weeklySalesCount();
            Map<String, Long> monthlyCounts = monthlySalesCount();
            Map<String, Long> yearlyCounts = yearlySalesCount();


            String weeklyJson = mapToJsonSales(weekly);
            String monthlyJson = mapToJsonSales(monthly);
            String yearlyJson = mapToJsonSales(yearly);
            String dailyJson = mapToJsonSales(daily);
            String dailyCountJson = mapToJsonCount(dailyCounts);
            String weeklyCountJson = mapToJsonCount(weeklyCounts);
            String monthlyCountJson = mapToJsonCount(monthlyCounts);
            String yearlyCountJson = mapToJsonCount(yearlyCounts);


            model.addAttribute("totalSales", totalSales);
            model.addAttribute("soldProducts", soldProducts);
            model.addAttribute("activeCustomers", activeCustomers);
            model.addAttribute("topSellingProducts", topSellingProducts);
            model.addAttribute("daily", dailyJson);
            model.addAttribute("weekly", weeklyJson);
            model.addAttribute("monthly", monthlyJson);
            model.addAttribute("yearly", yearlyJson);
            model.addAttribute("dailyCount", dailyCountJson);
            model.addAttribute("weeklyCount", weeklyCountJson);
            model.addAttribute("monthlyCount", monthlyCountJson);
            model.addAttribute("yearlyCount", yearlyCountJson);

            return "Admin/dashboard";
        } catch (Exception e) {
            log.info("dashboard, " + e.getMessage());
            return "Exception/404";
        }

    }

    public String mapToJsonSales(Map<String, Double> mapObj) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(mapObj);
    }

    public String mapToJsonCount(Map<String, Long> mapObj) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(mapObj);
    }

    public List<Products> topSelling() {

        try {
//            By using manual method

//            List<OrderProducts> orderProductsList = orderProductRepository.findAll(); // Assuming this fetches all your order products
//
//            Map<Products, Integer> productQuantityMap = orderProductsList.stream()
//                    .collect(Collectors.groupingBy(OrderProducts::getProduct,
//                            Collectors.summingInt(OrderProducts::getQuantity)));
//
//            List<Products> topSellingProducts = productQuantityMap.entrySet().stream()
//                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                    .limit(10)
//                    .map(Map.Entry::getKey)
//                    .collect(Collectors.toList());
//
//            // Print the top-selling products
//            log.info("Top Selling Products:");
//            for (Products product : topSellingProducts) {
//                log.info(product.getName() + " - Total Quantity Sold: " + productQuantityMap.get(product));

//          By using Query
            return  orderProductRepository.findTopSellingProducts();
        } catch (Exception e) {
            log.info("top selling, " + e.getMessage());
            throw new QueryException(e.getMessage());
        }
    }

    public Map<String, Double> dailySales() {
        // Past 7 continuous daily sales including today
        Map<String, Double> dailySale = new LinkedHashMap<>();

        double orderTotalAmount ;

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();
            LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Orders> list = orderRepository.getDailyFromCurrentDay(currentDate);

            orderTotalAmount = list.stream().mapToDouble(Orders::getTotalAmount).sum();

            dailySale.put(currentDate.toString(), orderTotalAmount);

            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        return dailySale;
    }

    public Map<String, Long> dailySalesCount() {
        // Past 7 continuous daily sales including today
        Map<String, Long> dailySaleCount = new LinkedHashMap<>();


        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();
            LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Orders> list = orderRepository.getDailyFromCurrentDay(currentDate);

            Long count = list.stream()
                    .filter(order -> !order.getOrderProducts().isEmpty())
                    .count();


            dailySaleCount.put(currentDate.toString(), count);

            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        return dailySaleCount;
    }

    public Map<String, Double> weeklySales() {
        // Past 7 continuous weekly sales
        Map<String, Double> weeklySales = new LinkedHashMap<>();

        Calendar calendar = Calendar.getInstance();

        for (int i = 1; i <= 7; i++) {
            Date weekEndDate = calendar.getTime();

            calendar.add(Calendar.WEEK_OF_YEAR, -i);

            Date weekStartDate = calendar.getTime();

            LocalDate weekStart = weekStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate weekEnd = weekEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Orders> list = orderRepository.getWeeklyFromStartToEnd(weekStart, weekEnd);

            double totalOrderAmount = list.stream().mapToDouble(Orders::getTotalAmount).sum();

            weeklySales.put(weekStart + "-" + weekEnd, totalOrderAmount);
        }

        return weeklySales;
    }

    public Map<String, Long> weeklySalesCount() {
        // Past 7 continuous weekly sales
        Map<String, Long> weeklySalesCount = new LinkedHashMap<>();

        Calendar calendar = Calendar.getInstance();

        for (int i = 1; i <= 7; i++) {
            Date weekEndDate = calendar.getTime();

            calendar.add(Calendar.WEEK_OF_YEAR, -i);

            Date weekStartDate = calendar.getTime();

            LocalDate weekStart = weekStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate weekEnd = weekEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Orders> list = orderRepository.getWeeklyFromStartToEnd(weekStart, weekEnd);

            Long count = list.stream()
                    .filter(order -> !order.getOrderProducts().isEmpty())
                    .count();
            weeklySalesCount.put(weekStart + "-" + weekEnd, count);
        }

        log.info("-----------------------" + weeklySalesCount);

        return weeklySalesCount;
    }

    public Map<String, Double> monthlySales() {
        Map<String, Double> monthlySales = new LinkedHashMap<>();

        YearMonth currentYearMonth = YearMonth.now();
        Month currentMonth = currentYearMonth.getMonth();

        for (int i = currentMonth.getValue() - 1; i >= 0; i--) {
            YearMonth targetYearMonth = currentYearMonth.minusMonths(i);
            Month targetMonth = targetYearMonth.getMonth();

            LocalDate monthStart = targetYearMonth.atDay(1);
            LocalDate monthEnd = targetYearMonth.atEndOfMonth();

            List<Orders> list = orderRepository.getMonthlyFromStartToEnd(monthStart, monthEnd);

            double totalOrderAmount = list.stream().mapToDouble(Orders::getTotalAmount).sum();

            monthlySales.put(targetMonth.toString(), totalOrderAmount);
        }

        return monthlySales;
    }

    public Map<String, Long> monthlySalesCount() {
        Map<String, Long> monthlySalesCount = new LinkedHashMap<>();

        YearMonth currentYearMonth = YearMonth.now();
        Month currentMonth = currentYearMonth.getMonth();

        for (int i = currentMonth.getValue() - 1; i >= 0; i--) {
            YearMonth targetYearMonth = currentYearMonth.minusMonths(i);
            Month targetMonth = targetYearMonth.getMonth();

            LocalDate monthStart = targetYearMonth.atDay(1);
            LocalDate monthEnd = targetYearMonth.atEndOfMonth();

            List<Orders> list = orderRepository.getMonthlyFromStartToEnd(monthStart, monthEnd);

            Long count = list.stream()
                    .filter(order -> !order.getOrderProducts().isEmpty())
                    .count();

            monthlySalesCount.put(targetMonth.toString(), count);
        }

        return monthlySalesCount;
    }

    public Map<String, Double> yearlySales() {
        Map<String, Double> yearlySales = new LinkedHashMap<>();

        Year currentYear = Year.now();

        for (int i = 4; i >= 0; i--) {
            Year targetYear = currentYear.minusYears(i);

            LocalDate yearStart = targetYear.atDay(1);
            LocalDate yearEnd = targetYear.atMonth(Month.DECEMBER).atEndOfMonth();

            List<Orders> list = orderRepository.getYearlyFromStartToEnd(yearStart, yearEnd);

            double totalOrderAmount = list.stream().mapToDouble(Orders::getTotalAmount).sum();

            yearlySales.put(Integer.toString(targetYear.getValue()), totalOrderAmount);
        }
        return yearlySales;
    }

    public Map<String, Long> yearlySalesCount() {
        Map<String, Long> yearlySalesCount = new LinkedHashMap<>();

        Year currentYear = Year.now();

        for (int i = 4; i >= 0; i--) {
            Year targetYear = currentYear.minusYears(i);

            LocalDate yearStart = targetYear.atDay(1);
            LocalDate yearEnd = targetYear.atMonth(Month.DECEMBER).atEndOfMonth();

            List<Orders> list = orderRepository.getYearlyFromStartToEnd(yearStart, yearEnd);

            Long count = list.stream()
                    .filter(order -> !order.getOrderProducts().isEmpty())
                    .count();

            yearlySalesCount.put(Integer.toString(targetYear.getValue()), count);
        }

        return yearlySalesCount;
    }
}

