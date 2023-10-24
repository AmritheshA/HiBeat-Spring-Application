package com.Hibeat.Hibeat.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "OrderProducts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Orders orders;

    @ManyToOne
    private Products product;

//    private double productPriceWhenOrdering;

    @Column(name = "quantity")
    private int quantity=1;

}
