package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Orders;
import com.Hibeat.Hibeat.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders ,Integer> {

    Orders findByOrderId(String orderId);

    List<Orders> findByUser(User user);

}
