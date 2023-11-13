package com.Hibeat.Hibeat.Model.User;

import com.Hibeat.Hibeat.Model.Admin.Products;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CartProduct")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Cart cart;

    @ManyToOne
    private Products product;

    @Column(name = " quantity")
    private int quantity=1;


}
