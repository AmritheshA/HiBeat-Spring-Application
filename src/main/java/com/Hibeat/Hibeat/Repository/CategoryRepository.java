package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {

    List<Categories> findAll();

    List<Categories> findByCategoryNameContaining(String searchKey);
    

}
