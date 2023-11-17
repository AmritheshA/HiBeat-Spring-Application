package com.Hibeat.Hibeat.Model.Admin;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productOffer")
@Setter
@Getter
public class ProductOffers {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productOff_sequence")
    @SequenceGenerator(name = "productOff_sequence", sequenceName = "productOff_sequence", allocationSize = 1)
    @Column(name = "offerId")
    private Integer offerId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Products products;

    @Column(name = "percentage")
    private double offerPercentage;

    @Column(name = "discountAmount")
    private double discountAmount;

    @Column(name = "expiryDate")
    private String expiryDate;
}