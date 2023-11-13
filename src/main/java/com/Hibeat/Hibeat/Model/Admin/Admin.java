package com.Hibeat.Hibeat.Model.Admin;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admins")
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_sequence")
    @SequenceGenerator(name = "admin_sequence", sequenceName = "admin_sequence", allocationSize = 1)
    @Column(name = "adminId")
    private Integer id;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private int mobile;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private  String[] address;

    @Column(name = "role")
    private String role;

}
