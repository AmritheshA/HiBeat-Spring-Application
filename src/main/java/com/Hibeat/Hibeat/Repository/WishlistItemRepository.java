package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Products;
import com.Hibeat.Hibeat.Model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem,Long> {

    Products findByProduct(Products products);
}
