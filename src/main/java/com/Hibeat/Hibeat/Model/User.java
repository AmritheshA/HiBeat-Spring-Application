package com.Hibeat.Hibeat.Model;

import jakarta.persistence.*;
import lombok.Data;



import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
//Adding Lombok
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "userid")
    private Integer id;

    @Column(name = "username")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Address> addresses;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "restToken")
    private String resetToken;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Orders> orders;

    @Column(name = "status")
    private String status= "UN-BLOCKED";

    @Column(name = "statusUpdatedTime")
    private LocalDateTime statusLastUpdated;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

}
