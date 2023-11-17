package com.Hibeat.Hibeat.Model.User;

import com.Hibeat.Hibeat.Model.Admin.Products;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "review")
@Setter
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_sequence")
    @SequenceGenerator(name = "review_sequence", sequenceName = "review_sequence", allocationSize = 1)
    @Column(name = "reviewId")
    private Integer id;

    @Column(name = "title")
    private String reviewTitle;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private double rating;

    @Column(name = "image")
    private String image;

//    @ElementCollection
//    @CollectionTable(name = "replays", joinColumns = @JoinColumn(name = "reviewId"))
//    @Column(name = "replays")
//    private List<String> replays = new ArrayList<>();

    @Column(name = "reviewTime")
    private LocalDate reviewTime;

    @ManyToOne
    private User user;

    @ManyToOne()
    private Products products;


}
