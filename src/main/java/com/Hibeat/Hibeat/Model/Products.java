package com.Hibeat.Hibeat.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "products")
@Data
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @Column(name = "product_id")
    private Integer id;

    @Column(name = "price")
    private double price;

    @Column(name = "selling_price")
    private String sellingPrice;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "images",columnDefinition = "text[]")
    private String[] images_path = new String[3];

    @Column(name = "rating")
    private float rating;

    @Column(name = "stock")
    private int stock;

    @Column(name = "add_By")
    private String addBy;

//    For soft Delete also
    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "category_id")
    private int categories;

    @Column(name = "sold_date")
    private Date date;

    @Column(name = "Type")
    private String type;

}

