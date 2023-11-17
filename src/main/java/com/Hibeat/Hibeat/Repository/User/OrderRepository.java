package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders ,Integer> {

    Orders findByOrderId(String orderId);

    List<Orders> findByUser(User user);

    Orders findByOrdersId(Integer ordersId);

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = 'Delivered' AND o.deliveredDate <= CURRENT_DATE")
    Integer totalSoldProducts();

    @Query("SELECT o FROM Orders o WHERE o.status = 'Delivered' AND o.deliveredDate = :currentDate")
    List<Orders> getDailyFromCurrentDay(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT o FROM Orders o WHERE o.status = 'Delivered' AND o.deliveredDate >= :weekStart AND o.deliveredDate <= :weekEnd")
    List<Orders> getWeeklyFromStartToEnd(@Param("weekStart") LocalDate weekStart, @Param("weekEnd") LocalDate weekEnd);

    @Query("SELECT o FROM Orders o WHERE o.status = 'Delivered' AND o.deliveredDate >= :monthStart AND o.deliveredDate <= :monthEnd")
    List<Orders> getMonthlyFromStartToEnd(@Param("monthStart") LocalDate monthStart, @Param("monthEnd") LocalDate monthEnd);

    @Query("SELECT o FROM Orders o WHERE o.status = 'Delivered' AND o.deliveredDate >= :yearStart AND o.deliveredDate <= :yearEnd")
    List<Orders> getYearlyFromStartToEnd(@Param("yearStart") LocalDate yearStart, @Param("yearEnd") LocalDate yearEnd);

    @Query("SELECT o FROM Orders o WHERE o.status = 'Delivered' AND o.deliveredDate >= :startDate AND o.deliveredDate <= :endDate")
    List<Orders> monthlySalesReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
