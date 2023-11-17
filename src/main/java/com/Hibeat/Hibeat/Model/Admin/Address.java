package com.Hibeat.Hibeat.Model.Admin;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Address")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullName")
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "House_No.")
    private String address;

    @Column(name = "locality")
    private String locality;

    @Column(name = "city")
    private String city;

    @Column(name = "pin")
    private String pin;
}