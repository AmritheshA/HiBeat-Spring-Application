package com.Hibeat.Hibeat.Controller.AdminController;

import com.Hibeat.Hibeat.Servicess.Admin_Service.AdminCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryController {

    private final AdminCategoryService categoryService;

    @Autowired
    public CategoryController(AdminCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String categories(@RequestParam(value = "searchKey",required = false) String searchKey,Model model) {
        return categoryService.getCategories(searchKey,model);
    }

    @GetMapping("/new-categories")
    public String newCategories(@RequestParam("newCategory") String newCategory) {
       return categoryService.newCategories(newCategory);
    }


    @GetMapping("/edit-categories")
    public String edit_categories(@RequestParam("id") int id,
                                  @RequestParam("newName") String newCategoryName) {
        return categoryService.editCategories(id,newCategoryName);
    }

    @GetMapping("/disable-category/{id}")
    public String disableCategory(@PathVariable("id") int id) {
        return categoryService.disableCategories(id);
    }

    @GetMapping("/new-brand")
    public String newBrands(@RequestParam("newBrand") String newBrands) {
        return categoryService.addBrand(newBrands);
    }


    @GetMapping("/edit-brand")
    public String edit_Brand(@RequestParam("id") Integer id,
                             @RequestParam("newName") String newBrandName) {
        return categoryService.editBrand(id, newBrandName);
    }

    @GetMapping("/disable-brand/{id}")
    public String disableBrand(@PathVariable("id") Integer id) {
        return categoryService.disableBrands(id);
    }
}
