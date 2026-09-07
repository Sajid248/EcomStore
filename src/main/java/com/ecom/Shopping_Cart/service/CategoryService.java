package com.ecom.Shopping_Cart.service;

import com.ecom.Shopping_Cart.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    public Category saveCategory(Category category);

    public Boolean existCategory(String name);

    public List<Category> getAllCatgeory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCatgeory();

    public Page<Category> getAllCategoryPagination(Integer pageNo,Integer pageSize);

}
