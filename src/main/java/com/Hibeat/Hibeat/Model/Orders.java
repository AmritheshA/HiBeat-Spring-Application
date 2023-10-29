package com.Hibeat.Hibeat.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @Column(name = "ordersId")
    private Integer ordersId;

    @Column(name = "orderId")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id") // Adjust the column name as per your database schema
    private User user;

    @Column(name = "addressIndex")
    private int addressIndex;

    @OneToMany(mappedBy = "orders", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderProducts> orderProducts;

    @Column(name = "orderDate")
    private LocalDate orderDate;

    @Column(name = "amount")
    private double totalAmount;

    @OneToOne(mappedBy = "orders", cascade = {CascadeType.ALL},orphanRemoval = true)
    private Payments payments;

    @Column(name = "status")
    private String status = "ordered";

    @Column(name = "amountStatus")
    private String amountStatus;

    @Column(name = "delivereDate")
    private LocalDate deliveredDate;

    @Column(name = "returnExpiryDate")
    private LocalDate returnExpiryDate;

    @Column(name = "cancelled")
    private boolean cancelled = false;

    @Column(name = "refundStatus")
    private boolean refundStatus = false;










}
