package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatgeoryRepository extends JpaRepository<Category,Integer> {

    public Boolean existsByName(String name);

    public List<Category> findByIsActiveTrue();


}
