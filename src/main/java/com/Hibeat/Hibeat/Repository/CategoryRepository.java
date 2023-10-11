package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Categories;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {

    List<Categories> findAll();

    

}
