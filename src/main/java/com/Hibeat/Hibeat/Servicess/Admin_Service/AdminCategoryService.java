package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.Categories;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface AdminCategoryService {

    String getCategories(String searchKey, Model model);

    List<Categories> findAll(Sort id);

    List<Categories> findByCategoryNameContaining(String searchKey);

    String newCategories(String newCategory);

    String editCategories(int id, String newCategoryName);

    String disableCategories(int id);

    Optional<Categories> findById(int id);

    Categories save(Categories categories);

}
