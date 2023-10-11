package com.Hibeat.Hibeat.Model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

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
    private BigInteger mobile;

    @Column(name = "address")
    private String[] address;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "restToken")
    private String resetToken;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status= "UN-BLOCKED";

}
