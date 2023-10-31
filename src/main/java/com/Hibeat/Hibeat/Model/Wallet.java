package com.Hibeat.Hibeat.Model;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "wallet")
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_sequence")
    @SequenceGenerator(name = "wallet_sequence", sequenceName = "wallet_sequence", allocationSize = 1)
    @Column(name = "walletId")
    private Integer walletId;

    @OneToOne
    private User user;

    @Column(name = "walletTotalAmount")
    private double walletTotalAmount;

    @OneToMany(mappedBy = "wallet", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<WalletHistory> walletHistory;

}
