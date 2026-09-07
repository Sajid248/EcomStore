package com.ecom.Shopping_Cart.service.impl;

import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.repository.CatgeoryRepository;
import com.ecom.Shopping_Cart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CatgeoryServiceImpl implements CategoryService {

    @Autowired
    private CatgeoryRepository catgeoryRepository;


    @Override
    public Category saveCategory(Category category) {
        return catgeoryRepository.save(category);
    }

    @Override
    public Boolean existCategory(String name) {
        return catgeoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAllCatgeory() {
        return catgeoryRepository.findAll();
    }

    @Override
    public Boolean deleteCategory(int id) {
        Category category = catgeoryRepository.findById(id).orElse(null);

        if(!ObjectUtils.isEmpty(category))
        {
            catgeoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category = catgeoryRepository.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCatgeory() {
        List<Category> categories = catgeoryRepository.findByIsActiveTrue();
        return categories;
    }

    @Override
    public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return catgeoryRepository.findAll(pageable);
    }
}
