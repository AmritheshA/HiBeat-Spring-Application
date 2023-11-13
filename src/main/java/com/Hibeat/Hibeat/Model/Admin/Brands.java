package com.Hibeat.Hibeat.Model.Admin;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "brands")
@Data
public class Brands {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_sequence")
    @SequenceGenerator(name = "brand_sequence", sequenceName = "brand_sequence", allocationSize = 1)
    @Column(name = "brandId")
    private Integer id;

    @Column(name = "brand_name")
    private String categoryName;

    @Column(name = "status")
    private String status = "ACTIVE";
}
