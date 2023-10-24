package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts,Long> {


}
