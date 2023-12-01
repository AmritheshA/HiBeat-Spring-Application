package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.Model.User.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts,Long> {

    @Query("SELECT op.product, SUM(op.quantity) AS totalQuantity " +
            "FROM OrderProducts op " +
            "GROUP BY op.product " +
            "ORDER BY totalQuantity DESC LIMIT 10")
   List<Products> findTopSellingProducts();

}
