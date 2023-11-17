package com.Hibeat.Hibeat.Repository.Admin;

import com.Hibeat.Hibeat.Model.Admin.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {

    List<Categories> findAll();

    List<Categories> findByCategoryNameContaining(String searchKey);

    List<Categories> findByCategoryName(String name);
    

}
