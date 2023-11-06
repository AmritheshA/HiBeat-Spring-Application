package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Categories;
import com.Hibeat.Hibeat.Repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminCategoryServiceImp implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminCategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Categories> findAll(Sort id) {
        return categoryRepository.findAll();
    }

    @Override
    public List<Categories> findByCategoryNameContaining(String searchKey) {
        return categoryRepository.findByCategoryNameContaining(searchKey);
    }

    @Override
    public Optional<Categories> findById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Categories save(Categories categories) {
        return categoryRepository.save(categories);
    }

    @Override
    public String getCategories(String searchKey, Model model) {
        try {

            List<Categories> category = findAll((Sort.by(Sort.Direction.ASC, "id")));
            List<Categories> categories = findByCategoryNameContaining(searchKey);

            category.sort(Comparator.comparing(Categories::getId));
            categories.sort(Comparator.comparing(Categories::getId));

            if (searchKey != null) {
                model.addAttribute("categories", categories);
            } else if (!(category.isEmpty())) {
                model.addAttribute("categories", category);
            }

            return "Admin/categories";
        } catch (Exception e) {
            log.info("getCategories" + e.getMessage());
            return "Exception/404";
        }

    }


    @Override
    public String newCategories(String newCategory) {

        Categories categories = new Categories();
        categories.setCategoryName(newCategory);
        save(categories);

        return "redirect:/admin/categories";
    }

    @Override
    public String editCategories(int id, String newCategoryName) {
        try {

            Optional<Categories> category = findById(id);
            Categories categories = category.get();

            if (categories.getStatus().equals("ACTIVE")) {

                categories.setCategoryName(newCategoryName);

                log.info("updated Categories" + newCategoryName);
                categoryRepository.save(categories);
            }

            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.info("editCategories" + e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    public String disableCategories(int id) {
        try {
            Optional<Categories> categories = findById(id);

            if (categories.isPresent()) {

                Categories category = categories.get();

                if (category.getStatus().equals("ACTIVE")) {

                    category.setStatus("IN-ACTIVE");

                } else if (category.getStatus().equals("IN-ACTIVE")) {

                    category.setStatus("ACTIVE");
                }
                save(category);
            }

            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.info("disableCategories" + e.getMessage());
            return "Exception/404";
        }
    }
}
