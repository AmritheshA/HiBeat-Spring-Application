package com.Hibeat.Hibeat.Model.Admin;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "banner")
@Data
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banner_sequence")
    @SequenceGenerator(name = "banner_sequence", sequenceName = "banner_sequence", allocationSize = 1)
    @Column(name = "bannerId")
    private Integer bannerId;

    @Column(name = "title")
    private String bannerTitle;

    @Column(name = "bannerStatus")
    private String bannerStatus;

    @Column(name = "image")
    private String image;
}
