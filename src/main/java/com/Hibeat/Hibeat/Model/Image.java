package com.Hibeat.Hibeat.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sequence")
    @SequenceGenerator(name = "image_sequence", sequenceName = "image_sequence", allocationSize = 1)
    @Column(name = "imageId")
    private int id;

    @Column(name = "main_image")
    private String image;

    @Column(name = "thumbnail1")
    private String thumbnail1;

    @Column(name = "thumbnail2")
    private String thumbnail2;

}
