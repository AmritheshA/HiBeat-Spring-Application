package com.Hibeat.Hibeat.Model.Admin;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupons")
@Data
public class Coupons {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupons_sequence")
    @SequenceGenerator(name = "coupons_sequence", sequenceName = "coupons_sequence", allocationSize = 1)
    @Column(name = "couponsId")
    private Integer id;

    @Column(name = "couponName")
    private String couponName;

    @Column(name = "code")
    private String couponCode;

    @Column(name = "exprieTime")
    private LocalDate expireTime;

    @Column(name = "status")
    private String status;

    @Column(name = "minimumAmount")
    private double minimumAmount;

    @Column(name = "discountAmount")
    private double discountAmount;

    @Column(name = "singleOrMultiple")
    private String singleOrMultiple;

    @ElementCollection
    private Set<Integer> userid = new HashSet<>();



}
