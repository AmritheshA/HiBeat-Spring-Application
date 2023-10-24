package com.Hibeat.Hibeat.Model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.extern.slf4j.XSlf4j;

import java.util.List;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_sequence")
    @SequenceGenerator(name = "cart_sequence", sequenceName = "cart_sequence", allocationSize = 1)
    @Column(name = "cartId")
    private Integer id;


    @OneToOne
    @JoinColumn(name = "cart_user_id" )
    private User user;


    @OneToMany(mappedBy = "cart", cascade = {CascadeType.ALL},orphanRemoval = true)
    private List<CartProduct> cartProducts;

    @Column(name = "totalCartAmount")
    private double totalCartAmount = 0.0;

}