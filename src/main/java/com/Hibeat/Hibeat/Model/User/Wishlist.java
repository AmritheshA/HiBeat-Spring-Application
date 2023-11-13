package com.Hibeat.Hibeat.Model.User;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "wishlist")
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishlist_sequence")
    @SequenceGenerator(name = "wishlist_sequence", sequenceName = "wishlist_sequence", allocationSize = 1)
    @Column(name = "wishlistId")
    private Integer id;


    @OneToOne
    @JoinColumn(name = "wishlist_user_id" )
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = {CascadeType.ALL},orphanRemoval = true)
    private List<WishlistItem> wishlistItems;
}
