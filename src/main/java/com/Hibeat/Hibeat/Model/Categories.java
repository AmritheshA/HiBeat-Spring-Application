package com.Hibeat.Hibeat.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cat_sequence")
    @SequenceGenerator(name = "cat_sequence", sequenceName = "cat_sequence", allocationSize = 1)
    @Column(name = "categoryId")
    private Integer id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "status")
    private String status = "ACTIVE";
}
